package org.example.commandeservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientNom;
    private String clientEmail;
    private LocalDateTime dateCommande;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "commande")
    private List<LigneCommande> lignes = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    private Double montantTotal;
}

