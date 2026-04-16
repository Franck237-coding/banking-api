package com.banking.controller;

import com.banking.model.Account;
import com.banking.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Gestion des Comptes", description = "API pour la gestion des comptes bancaires")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @Operation(summary = "Créer un nouveau compte", description = "Crée un nouveau compte bancaire")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Compte créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "409", description = "Numéro de compte déjà utilisé")
    })
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        try {
            Account createdAccount = accountService.createAccount(account);
            return ResponseEntity.status(201).body(createdAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Lister tous les comptes", description = "Retourne la liste de tous les comptes bancaires")
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    
    @Operation(summary = "Récupérer un compte par ID", description = "Retourne les détails d'un compte spécifique")
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(
            @Parameter(description = "ID du compte à récupérer") @PathVariable Long id) {
        Optional<Account> account = accountService.getAccountById(id);
        return account.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Récupérer un compte par numéro", description = "Retourne les détails d'un compte via son numéro")
    @GetMapping("/numero/{numeroCompte}")
    public ResponseEntity<Account> getAccountByNumero(
            @Parameter(description = "Numéro du compte à récupérer") @PathVariable String numeroCompte) {
        Optional<Account> account = accountService.getAccountByNumeroCompte(numeroCompte);
        return account.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Lister les comptes d'un utilisateur", description = "Retourne tous les comptes d'un utilisateur spécifique")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUser(
            @Parameter(description = "ID de l'utilisateur") @PathVariable Long userId) {
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }
    
    @Operation(summary = "Mettre à jour un compte", description = "Met à jour les informations d'un compte")
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
            @Parameter(description = "ID du compte à mettre à jour") @PathVariable Long id,
            @Valid @RequestBody Account accountDetails) {
        try {
            Account updatedAccount = accountService.updateAccount(id, accountDetails);
            return ResponseEntity.ok(updatedAccount);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Supprimer un compte", description = "Supprime un compte bancaire")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "ID du compte à supprimer") @PathVariable Long id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @Operation(summary = "Effectuer un dépôt", description = "Dépose de l'argent sur un compte")
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
            @Parameter(description = "Numéro du compte") @RequestParam String numeroCompte,
            @Parameter(description = "Montant à déposer") @RequestParam BigDecimal montant) {
        boolean success = accountService.deposit(numeroCompte, montant);
        if (success) {
            return ResponseEntity.ok("Dépôt effectué avec succès");
        } else {
            return ResponseEntity.badRequest().body("Échec du dépôt - compte non trouvé");
        }
    }
    
    @Operation(summary = "Effectuer un retrait", description = "Retire de l'argent d'un compte")
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(
            @Parameter(description = "Numéro du compte") @RequestParam String numeroCompte,
            @Parameter(description = "Montant à retirer") @RequestParam BigDecimal montant) {
        boolean success = accountService.withdraw(numeroCompte, montant);
        if (success) {
            return ResponseEntity.ok("Retrait effectué avec succès");
        } else {
            return ResponseEntity.badRequest().body("Échec du retrait - compte non trouvé ou solde insuffisant");
        }
    }
}
