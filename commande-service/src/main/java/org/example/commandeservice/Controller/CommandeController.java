package org.example.commandeservice.Controller;

import org.example.commandeservice.DTO.CommandeRequest;
import org.example.commandeservice.Entity.Commande;
import org.example.commandeservice.Repository.CommandeRepository;
import org.example.commandeservice.Service.CommandeService;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;
    private final CommandeRepository commandeRepository;

    @PostMapping
    public ResponseEntity<Commande> creerCommande(@RequestBody CommandeRequest request) {
        try {
            Commande commande = commandeService.creerCommande(request);
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommande(@PathVariable Long id) {
        return commandeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/{email}")
    public List<Commande> getCommandesByClient(@PathVariable String email) {
        return commandeRepository.findByClientEmail(email);
    }
}