package org.example.livraisonservice.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.livraisonservice.DTO.NominatimReponse;
import org.example.livraisonservice.DTO.OsrmResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeolocationService {

    private final WebClient.Builder webClientBuilder;
    public Mono<NominatimReponse> getCoordinates(String adresse, String ville, String codePostal, String pays) {
        return webClientBuilder.baseUrl("http://geolocation-service").build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/geolocation/coordinates")
                        .queryParam("adresse", adresse)
                        .queryParam("ville", ville)
                        .queryParam("codePostal", codePostal)
                        .queryParam("pays", pays)
                        .build())
                .retrieve()
                .bodyToMono(NominatimReponse.class)
                .doOnError(e -> log.error("Error calling geolocation service for coordinates", e));
    }

    public Mono<OsrmResponse> getRoute(Double startLat, Double startLon, Double endLat, Double endLon) {
        return webClientBuilder.baseUrl("http://geolocation-service").build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/geolocation/route")
                        .queryParam("startLat", startLat)
                        .queryParam("startLon", startLon)
                        .queryParam("endLat", endLat)
                        .queryParam("endLon", endLon)
                        .build())
                .retrieve()
                .bodyToMono(OsrmResponse.class)
                .doOnError(e -> log.error("Error calling geolocation service for route", e));
    }

    public Double calculerDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        try {
            return webClientBuilder.baseUrl("http://geolocation-service").build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/geolocation/distance")
                            .queryParam("startLat", lat1)
                            .queryParam("startLon", lon1)
                            .queryParam("endLat", lat2)
                            .queryParam("endLon", lon2)
                            .build())
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block();
        } catch (Exception e) {
            log.error("Error calculating distance via service", e);
            return 0.0;
        }
    }
}
