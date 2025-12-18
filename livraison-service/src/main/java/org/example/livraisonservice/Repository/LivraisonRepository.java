package org.example.livraisonservice.Repository;

import org.example.livraisonservice.Entity.Livraison;
import org.example.livraisonservice.Entity.StatutLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
    Optional<Livraison> findByCommandeId(Long commandeId);
    List<Livraison> findByStatut(StatutLivraison statut);
    List<Livraison> findByVille(String ville);
}