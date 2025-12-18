package org.example.produitservice.config;


import org.example.produitservice.entity.Produit;
import org.example.produitservice.repository.ProduitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ProduitRepository repository) {
        return args -> {
            repository.save(new Produit(null, "Paracétamol 500mg", "Antidouleur", 5.50, 100, "Médicament", false));
            repository.save(new Produit(null, "Ibuprofène 400mg", "Anti-inflammatoire", 7.80, 75, "Médicament", false));
            repository.save(new Produit(null, "Amoxicilline 1g", "Antibiotique", 12.50, 50, "Médicament", true));
            repository.save(new Produit(null, "Vitamine C", "Complément alimentaire", 8.90, 200, "Complément", false));
            repository.save(new Produit(null, "Masques chirurgicaux", "Protection", 15.00, 500, "Équipement", false));
        };
    }
}