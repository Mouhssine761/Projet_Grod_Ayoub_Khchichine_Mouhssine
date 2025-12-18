package org.example.livraisonservice.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.livraisonservice.DTO.LivraisonRequest;
import org.example.livraisonservice.DTO.LivraisonRequest;
import org.example.livraisonservice.DTO.NominatimReponse;
import org.example.livraisonservice.DTO.OsrmResponse;
import org.example.livraisonservice.Entity.Livraison;
import org.springframework.beans.factory.annotation.Value;
import org.example.livraisonservice.Entity.StatutLivraison;
import org.example.livraisonservice.Repository.LivraisonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LivraisonService {

    private final LivraisonRepository livraisonRepository;
    private final GeolocationService geolocationService;

    @Value("${warehouse.latitude}")
    private Double warehouseLat;

    @Value("${warehouse.longitude}")
    private Double warehouseLon;

    @Transactional
    public Livraison creerLivraison(LivraisonRequest request) {
        Livraison livraison = new Livraison();
        livraison.setCommandeId(request.getCommandeId());
        livraison.setAdresse(request.getAdresse());
        livraison.setVille(request.getVille());
        livraison.setCodePostal(request.getCodePostal());
        livraison.setPays(request.getPays());
        livraison.setTransporteur(request.getTransporteur());
        livraison.setStatut(StatutLivraison.EN_ATTENTE);
        livraison.setDateCreation(LocalDateTime.now());

        // Initialisation par défaut (sera écrasé si OSRM réussit)
        livraison.setDateLivraisonEstimee(LocalDateTime.now().plusDays(3));

        // Appel asynchrone à l'API de géolocalisation
        try {
            NominatimReponse geoResponse = geolocationService
                    .getCoordinates(request.getAdresse(), request.getVille(),
                            request.getCodePostal(), request.getPays())
                    .block(); // Block pour synchroniser la création (idéalement utiliser reactive flow complet)

            if (geoResponse != null) {
                double destLat = Double.parseDouble(geoResponse.getLat());
                double destLon = Double.parseDouble(geoResponse.getLon());
                
                livraison.setLatitude(destLat);
                livraison.setLongitude(destLon);
                log.info("Coordonnées trouvées: {}, {}", destLat, destLon);
                
                // Calculer l'itinéraire réel depuis l'entrepôt
                try {
                    OsrmResponse routeResponse = geolocationService.getRoute(warehouseLat, warehouseLon, destLat, destLon).block();
                    
                    if (routeResponse != null && routeResponse.getRoutes() != null && !routeResponse.getRoutes().isEmpty()) {
                        OsrmResponse.Route route = routeResponse.getRoutes().get(0);
                        
                        // Distance en mètres -> conversion en km
                        if (route.getDistance() != null) {
                            livraison.setDistanceKm(route.getDistance() / 1000.0);
                        }
                        
                        // Durée en secondes -> ajout au temps actuel + marge de préparation (ex: 2h)
                        if (route.getDuration() != null) {
                            long durationSeconds = route.getDuration().longValue();
                            long preparationSeconds = 7200; // 2 heures de préparation
                            livraison.setDateLivraisonEstimee(LocalDateTime.now().plusSeconds(durationSeconds + preparationSeconds));
                        }
                    }
                } catch (Exception ex) {
                    log.error("Erreur lors du calcul d'itinéraire OSRM: {}", ex.getMessage());
                    // Fallback: Haversine si OSRM échoue
                    double dist = geolocationService.calculerDistance(warehouseLat, warehouseLon, destLat, destLon);
                    livraison.setDistanceKm(dist);
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de la géolocalisation: {}", e.getMessage());
        }

        return livraisonRepository.save(livraison);
    }

    @Transactional
    public Livraison updateStatut(Long id, StatutLivraison nouveauStatut) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));

        livraison.setStatut(nouveauStatut);

        if (nouveauStatut == StatutLivraison.LIVREE) {
            livraison.setDateLivraisonReelle(LocalDateTime.now());
        }

        return livraisonRepository.save(livraison);
    }
}

