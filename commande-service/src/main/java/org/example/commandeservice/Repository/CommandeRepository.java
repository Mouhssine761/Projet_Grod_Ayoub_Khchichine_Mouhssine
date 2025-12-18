package org.example.commandeservice.Repository;


import org.example.commandeservice.Entity.Commande;
import org.example.commandeservice.Entity.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClientEmail(String email);
    List<Commande> findByStatut(StatutCommande statut);
}