package com.banking.controller;

import com.banking.dto.AccountDTO;
import com.banking.model.Account;
import com.banking.model.Bank;
import com.banking.model.User;
import com.banking.repository.BankRepository;
import com.banking.repository.UserRepository;
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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Gestion des Comptes", description = "API pour la gestion des comptes bancaires")
public class AccountController {
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BankRepository bankRepository;
    
    @Operation(summary = "Créer un nouveau compte", description = "Crée un nouveau compte bancaire dans une banque spécifique")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Compte créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Utilisateur ou banque non trouvé")
    })
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountDTO dto) {
        try {
            Optional<User> user = userRepository.findById(dto.getUserId());
            Optional<Bank> bank = bankRepository.findById(dto.getBankId());
            
            if (user.isEmpty() || bank.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            if (!user.get().getBank().getId().equals(bank.get().getId())) {
                throw new RuntimeException("L'utilisateur n'appartient pas à cette banque");
            }
            
            Account account = new Account();
            account.setNumeroCompte(dto.getNumeroCompte());
            account.setTypeCompte(Account.TypeCompte.valueOf(dto.getTypeCompte()));
            account.setSolde(dto.getSolde());
            account.setUser(user.get());
            account.setBank(bank.get());
            
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
            @Valid @RequestBody AccountDTO dto) {
        try {
            Optional<Account> existingAccount = accountService.getAccountById(id);
            Optional<Bank> bank = bankRepository.findById(dto.getBankId());
            
            if (existingAccount.isEmpty() || bank.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Account account = existingAccount.get();
            account.setTypeCompte(Account.TypeCompte.valueOf(dto.getTypeCompte()));
            account.setSolde(dto.getSolde());
            account.setBank(bank.get());
            
            Account updatedAccount = accountService.updateAccount(id, account);
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
}