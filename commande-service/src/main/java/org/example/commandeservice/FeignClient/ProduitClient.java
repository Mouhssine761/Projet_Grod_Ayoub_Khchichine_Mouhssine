package org.example.commandeservice.FeignClient;

import org.example.commandeservice.DTO.ProduitDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "produit-service")
public interface ProduitClient {

    @GetMapping("/api/produits/{id}")
    ProduitDTO getProduitById(@PathVariable("id") Long id);}