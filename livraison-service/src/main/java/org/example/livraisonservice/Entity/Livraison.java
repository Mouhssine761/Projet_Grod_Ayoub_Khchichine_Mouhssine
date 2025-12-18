package org.example.livraisonservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long commandeId;
    private String adresse;
    private String ville;
    private String codePostal;
    private String pays;

    // Coordonnées géographiques obtenues via API
    private Double latitude;
    private Double longitude;
    
    // Distance calculée en km
    private Double distanceKm;

    @Enumerated(EnumType.STRING)
    private StatutLivraison statut;

    private LocalDateTime dateCreation;
    private LocalDateTime dateLivraisonEstimee;
    private LocalDateTime dateLivraisonReelle;

    private String livreurNom;
    private String transporteur;
}

