package com.banking.controller;

import com.banking.model.Transaction;
import com.banking.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Gestion des Transactions", description = "API pour la gestion des transactions bancaires")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @Operation(summary = "Créer une transaction", description = "Crée une nouvelle transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Transaction créée avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        try {
            Transaction createdTransaction = transactionService.createTransaction(transaction);
            return ResponseEntity.status(201).body(createdTransaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
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
    
    @Operation(summary = "Effectuer un virement", description = "Effectue un virement entre deux comptes")
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> effectuerVirement(
            @Parameter(description = "Numéro du compte source") @RequestParam String compteSource,
            @Parameter(description = "Numéro du compte destination") @RequestParam String compteDestination,
            @Parameter(description = "Montant du virement") @RequestParam BigDecimal montant,
            @Parameter(description = "Description du virement") @RequestParam(required = false) String description) {
        try {
            Transaction transaction = transactionService.effectuerVirement(compteSource, compteDestination, montant, description);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
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
    
    @Operation(summary = "Transactions par période", description = "Retourne les transactions sur une période donnée")
    @GetMapping("/period")
    public ResponseEntity<List<Transaction>> getTransactionsByPeriod(
            @Parameter(description = "Date de début") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @Parameter(description = "Date de fin") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<Transaction> transactions = transactionService.getTransactionsByDateRange(debut, fin);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Transactions par statut", description = "Retourne les transactions par statut")
    @GetMapping("/status/{statut}")
    public ResponseEntity<List<Transaction>> getTransactionsByStatus(
            @Parameter(description = "Statut de la transaction") @PathVariable Transaction.StatutTransaction statut) {
        List<Transaction> transactions = transactionService.getTransactionsByStatus(statut);
        return ResponseEntity.ok(transactions);
    }
    
    @Operation(summary = "Transactions par type", description = "Retourne les transactions par type")
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Transaction>> getTransactionsByType(
            @Parameter(description = "Type de transaction") @PathVariable Transaction.TypeTransaction type) {
        List<Transaction> transactions = transactionService.getTransactionsByType(type);
        return ResponseEntity.ok(transactions);
    }
}
