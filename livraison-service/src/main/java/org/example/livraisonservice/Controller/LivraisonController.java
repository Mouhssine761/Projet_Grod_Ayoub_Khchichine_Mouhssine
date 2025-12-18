package org.example.livraisonservice.Controller;


import lombok.RequiredArgsConstructor;
import org.example.livraisonservice.DTO.LivraisonRequest;
import org.example.livraisonservice.Entity.Livraison;
import org.example.livraisonservice.Entity.StatutLivraison;
import org.example.livraisonservice.Repository.LivraisonRepository;
import org.example.livraisonservice.Service.LivraisonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/livraisons")
@RequiredArgsConstructor
public class LivraisonController {

    private final LivraisonService livraisonService;
    private final LivraisonRepository livraisonRepository;

    @org.springframework.beans.factory.annotation.Value("${warehouse.latitude}")
    private Double warehouseLat;

    @org.springframework.beans.factory.annotation.Value("${warehouse.longitude}")
    private Double warehouseLon;

    @GetMapping("/config")
    public java.util.Map<String, Double> getConfig() {
        return java.util.Map.of(
            "latitude", warehouseLat,
            "longitude", warehouseLon
        );
    }

    @PostMapping
    public ResponseEntity<Livraison> creerLivraison(@RequestBody LivraisonRequest request) {
        try {
            Livraison livraison = livraisonService.creerLivraison(request);
            return ResponseEntity.ok(livraison);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livraison> getLivraison(@PathVariable Long id) {
        return livraisonRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<Livraison> getLivraisonByCommande(@PathVariable Long commandeId) {
        return livraisonRepository.findByCommandeId(commandeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<Livraison> updateStatut(
            @PathVariable Long id,
            @RequestParam StatutLivraison statut) {
        try {
            Livraison livraison = livraisonService.updateStatut(id, statut);
            return ResponseEntity.ok(livraison);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
