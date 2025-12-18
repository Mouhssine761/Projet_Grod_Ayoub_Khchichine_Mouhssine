package org.example.produitservice.repository;
import org.example.produitservice.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    List<Produit> findByCategorie(String categorie);
    List<Produit> findByQuantiteStockGreaterThan(Integer quantite);
}

