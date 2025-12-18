package org.example.livraisonservice.DTO;
import lombok.Data;

@Data
public class LivraisonRequest {
    private Long commandeId;
    private String adresse;
    private String ville;
    private String codePostal;
    private String pays;
    private String transporteur;
}
