package org.example.commandeservice.DTO;

import lombok.Data;
import java.util.List;

@Data
public class CommandeRequest {
    private String clientNom;
    private String clientEmail;
    private List<LigneCommandeRequest> lignes;

    @Data
    public static class LigneCommandeRequest {
        private Long produitId;
        private Integer quantite;
    }
}

