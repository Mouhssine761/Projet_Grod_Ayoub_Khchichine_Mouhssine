package org.example.commandeservice.Service;


import org.example.commandeservice.DTO.CommandeRequest;
import org.example.commandeservice.DTO.ProduitDTO;
import org.example.commandeservice.Entity.Commande;
import org.example.commandeservice.Entity.LigneCommande;
import org.example.commandeservice.Entity.StatutCommande;
import org.example.commandeservice.FeignClient.ProduitClient;
import org.example.commandeservice.Repository.CommandeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ProduitClient produitClient;

    @Transactional
    public Commande creerCommande(CommandeRequest request) {
        Commande commande = new Commande();
        commande.setClientNom(request.getClientNom());
        commande.setClientEmail(request.getClientEmail());
        commande.setDateCommande(LocalDateTime.now());
        commande.setStatut(StatutCommande.EN_ATTENTE);
        commande.setLignes(new ArrayList<>());

        double montantTotal = 0.0;

        for (CommandeRequest.LigneCommandeRequest ligneReq : request.getLignes()) {
            ProduitDTO produit = produitClient.getProduitById(ligneReq.getProduitId());

            if (produit.getQuantiteStock() < ligneReq.getQuantite()) {
                throw new RuntimeException("Stock insuffisant pour le produit: " + produit.getNom());
            }

            LigneCommande ligne = new LigneCommande();
            ligne.setProduitId(produit.getId());
            ligne.setProduitNom(produit.getNom());
            ligne.setQuantite(ligneReq.getQuantite());
            ligne.setPrixUnitaire(produit.getPrix());
            ligne.setCommande(commande);

            commande.getLignes().add(ligne);
            montantTotal += produit.getPrix() * ligneReq.getQuantite();
        }

        commande.setMontantTotal(montantTotal);
        commande.setStatut(StatutCommande.CONFIRMEE);

        return commandeRepository.save(commande);
    }
}

