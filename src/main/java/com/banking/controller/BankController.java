package com.banking.controller;

import com.banking.dto.BankDTO;
import com.banking.model.Bank;
import com.banking.service.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/banks")
@Tag(name = "Gestion des Banques", description = "API pour la gestion des banques")
public class BankController {
    
    @Autowired
    private BankService bankService;
    
    @Operation(summary = "Créer une banque", description = "Crée une nouvelle banque")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Banque créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<Bank> createBank(@Valid @RequestBody BankDTO dto) {
        try {
            Bank bank = new Bank();
            bank.setNom(dto.getNom());
            bank.setCode(dto.getCode());
            bank.setAdresse(dto.getAdresse());
            bank.setTelephone(dto.getTelephone());
            
            Bank createdBank = bankService.createBank(bank);
            return new ResponseEntity<>(createdBank, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @Operation(summary = "Lister toutes les banques", description = "Retourne la liste de toutes les banques")
    @GetMapping
    public ResponseEntity<List<Bank>> getAllBanks() {
        List<Bank> banks = bankService.getAllBanks();
        return ResponseEntity.ok(banks);
    }
    
    @Operation(summary = "Récupérer une banque par ID", description = "Retourne une banque spécifique")
    @GetMapping("/{id}")
    public ResponseEntity<Bank> getBankById(
            @Parameter(description = "ID de la banque") @PathVariable Long id) {
        Optional<Bank> bank = bankService.getBankById(id);
        return bank.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Récupérer une banque par code", description = "Retourne une banque par son code")
    @GetMapping("/code/{code}")
    public ResponseEntity<Bank> getBankByCode(
            @Parameter(description = "Code de la banque") @PathVariable String code) {
        Optional<Bank> bank = bankService.getBankByCode(code);
        return bank.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Mettre à jour une banque", description = "Met à jour une banque existante")
    @PutMapping("/{id}")
    public ResponseEntity<Bank> updateBank(
            @Parameter(description = "ID de la banque") @PathVariable Long id,
            @Valid @RequestBody BankDTO dto) {
        try {
            Bank bank = new Bank();
            bank.setNom(dto.getNom());
            bank.setCode(dto.getCode());
            bank.setAdresse(dto.getAdresse());
            bank.setTelephone(dto.getTelephone());
            
            Bank updatedBank = bankService.updateBank(id, bank);
            return ResponseEntity.ok(updatedBank);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Supprimer une banque", description = "Supprime une banque")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(
            @Parameter(description = "ID de la banque") @PathVariable Long id) {
        try {
            bankService.deleteBank(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}