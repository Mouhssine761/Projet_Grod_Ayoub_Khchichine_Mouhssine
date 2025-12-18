package org.example.geolocationservice.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.geolocationservice.DTO.NominatimReponse;
import org.example.geolocationservice.DTO.OsrmResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeolocationService {

    private final WebClient webClient;
    
    @Value("${osrm.api.url}")
    private String osrmApiUrl;

    public Mono<NominatimReponse> getCoordinates(String adresse, String ville, String codePostal, String pays) {
        String query = String.format("%s, %s %s, %s", adresse, codePostal, ville, pays);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToFlux(NominatimReponse.class)
                .next()
                .doOnSuccess(response -> log.info("Géolocalisation réussie pour: {}", query))
                .doOnError(error -> log.error("Erreur de géolocalisation: {}", error.getMessage()));
    }

    /**
     * Calcule l'itinéraire réel entre deux points via OSRM
     */
    public Mono<OsrmResponse> getRoute(Double startLat, Double startLon, Double endLat, Double endLon) {
        // Format OSRM: /route/v1/driving/{lon1},{lat1};{lon2},{lat2}
        String coordinates = String.format("%s,%s;%s,%s", startLon, startLat, endLon, endLat);
        
        return WebClient.create(osrmApiUrl)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/route/v1/driving/" + coordinates)
                        .queryParam("overview", "false")
                        .build())
                .retrieve()
                .bodyToMono(OsrmResponse.class)
                .doOnSuccess(response -> log.info("Itinéraire calculé via OSRM"))
                .doOnError(error -> log.error("Erreur OSRM: {}", error.getMessage()));
    }

    public Double calculerDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        // Formule de Haversine pour calculer la distance entre deux points (fallback)
        final int R = 6371; // Rayon de la Terre en km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}
