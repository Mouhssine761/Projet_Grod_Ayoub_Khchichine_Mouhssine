package org.example.geolocationservice.Controller;

import lombok.RequiredArgsConstructor;
import org.example.geolocationservice.DTO.NominatimReponse;
import org.example.geolocationservice.DTO.OsrmResponse;
import org.example.geolocationservice.Service.GeolocationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/geolocation")
@RequiredArgsConstructor
public class GeolocationController {

    private final GeolocationService geolocationService;

    @GetMapping("/coordinates")
    public Mono<NominatimReponse> getCoordinates(
            @RequestParam String adresse,
            @RequestParam String ville,
            @RequestParam String codePostal,
            @RequestParam String pays) {
        return geolocationService.getCoordinates(adresse, ville, codePostal, pays);
    }

    @GetMapping("/route")
    public Mono<OsrmResponse> getRoute(
            @RequestParam Double startLat,
            @RequestParam Double startLon,
            @RequestParam Double endLat,
            @RequestParam Double endLon) {
        return geolocationService.getRoute(startLat, startLon, endLat, endLon);
    }
    
    @GetMapping("/distance")
    public Double getDistance(
            @RequestParam Double startLat,
            @RequestParam Double startLon,
            @RequestParam Double endLat,
            @RequestParam Double endLon) {
        return geolocationService.calculerDistance(startLat, startLon, endLat, endLon);
    }
}
