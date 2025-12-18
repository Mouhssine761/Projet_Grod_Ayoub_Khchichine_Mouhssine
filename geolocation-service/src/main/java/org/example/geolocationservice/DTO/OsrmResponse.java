package org.example.geolocationservice.DTO;

import lombok.*;
import java.util.List;

@Data
public class OsrmResponse {
    private String code;
    private List<Route> routes;

    @Data
    public static class Route {
        private String geometry;
        private List<Leg> legs;
        private Double distance;
        private Double duration;
    }

    @Data
    public static class Leg {
        private Double distance;
        private Double duration;
        private String summary;
    }
}
