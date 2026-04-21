package com.banking.controller;

import com.banking.model.Transaction;
import com.banking.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Gestion des Transactions", description = "API pour la gestion des transactions bancaires")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Operation(summary = "Effectuer un dépôt", description = "Effectue un dépôt sur un compte bancaire")
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> effectuerDepot(
            @Parameter(description = "Numéro du compte") @RequestParam String numeroCompte,
            @Parameter(description = "Montant du dépôt") @RequestParam BigDecimal montant,
            @Parameter(description = "Description du dépôt") @RequestParam(required = false) String description) {
        try {
            Transaction transaction = transactionService.effectuerDepot(numeroCompte, montant, description);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @Operation(summary = "Effectuer un retrait", description = "Effectue un retrait d'un compte bancaire")
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> effectuerRetrait(
            @Parameter(description = "Numéro du compte") @RequestParam String numeroCompte,
            @Parameter(description = "Montant du retrait") @RequestParam BigDecimal montant,
            @Parameter(description = "Description du retrait") @RequestParam(required = false) String description) {
        try {
            Transaction transaction = transactionService.effectuerRetrait(numeroCompte, montant, description);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
    @Operation(summary = "Lister toutes les transactions", description = "Retourne la liste de toutes les transactions")
    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Récupérer une transaction par ID", description = "Retourne les détails d'une transaction spécifique")
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(
            @Parameter(description = "ID de la transaction") @PathVariable Long id) {
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    @Operation(summary = "Transactions d'un compte", description = "Retourne toutes les transactions d'un compte")
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccount(
            @Parameter(description = "ID du compte") @PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccount(accountId);
        return ResponseEntity.ok(transactions);
    }
}
