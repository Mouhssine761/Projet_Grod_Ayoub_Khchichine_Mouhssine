package org.example.livraisonservice.DTO;

import lombok.Data;

@Data
public class NominatimReponse {
    private String lat;
    private String lon;
    private String display_name;
    private String type;
}
