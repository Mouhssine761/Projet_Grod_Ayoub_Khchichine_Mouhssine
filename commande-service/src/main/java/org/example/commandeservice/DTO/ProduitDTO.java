package org.example.commandeservice.DTO;
import com.fasterxml.jackson.annotation.JsonProperty; // Import this
import lombok.Data;

@Data
public class ProduitDTO {
    private Long id;
    private String nom;
    private String description;
    private Double prix;
    @JsonProperty("quantiteStock")
    private int quantiteStock;
    private String categorie;
    private Boolean prescriptionRequise;
}