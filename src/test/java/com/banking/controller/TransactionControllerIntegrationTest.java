package com.banking.controller;

import com.banking.model.Account;
import com.banking.model.Transaction;
import com.banking.model.User;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import com.banking.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TransactionControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private User testUser;
    private Account testAccount;
    
    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        
        testUser = new User();
        testUser.setNom("Dupont");
        testUser.setPrenom("Jean");
        testUser.setEmail("jean.dupont@example.com");
        testUser.setTelephone("0612345678");
        testUser = userRepository.save(testUser);
        
        testAccount = new Account();
        testAccount.setNumeroCompte("BANK1234567890");
        testAccount.setSolde(new BigDecimal("1000.00"));
        testAccount.setTypeCompte(Account.TypeCompte.COURANT);
        testAccount.setUser(testUser);
        testAccount = accountRepository.save(testAccount);
    }
    
    @Test
    void testEffectuerDepot_Success() throws Exception {
        mockMvc.perform(post("/api/transactions/deposit")
                .param("numeroCompte", "BANK1234567890")
                .param("montant", "500.00")
                .param("description", "Dépôt salaire"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeTransaction", is("DEPOT")))
                .andExpect(jsonPath("$.statut", is("EFFECTUEE")));
    }
    
    @Test
    void testEffectuerDepot_AccountNotFound() throws Exception {
        mockMvc.perform(post("/api/transactions/deposit")
                .param("numeroCompte", "INVALID")
                .param("montant", "500.00")
                .param("description", "Test"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testEffectuerRetrait_Success() throws Exception {
        mockMvc.perform(post("/api/transactions/withdraw")
                .param("numeroCompte", "BANK1234567890")
                .param("montant", "300.00")
                .param("description", "Retrait espèces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeTransaction", is("RETRAIT")))
                .andExpect(jsonPath("$.statut", is("EFFECTUEE")));
    }
    
    @Test
    void testEffectuerRetrait_InsufficientFunds() throws Exception {
        mockMvc.perform(post("/api/transactions/withdraw")
                .param("numeroCompte", "BANK1234567890")
                .param("montant", "5000.00")
                .param("description", "Test"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testGetAllTransactions() throws Exception {
        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
    
    @Test
    void testGetTransactionById_Found() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setReference("TXN1234567890");
        transaction.setMontant(new BigDecimal("100.00"));
        transaction.setTypeTransaction(Transaction.TypeTransaction.DEPOT);
        transaction.setCompteSource(testAccount);
        transaction.setStatut(Transaction.StatutTransaction.EFFECTUEE);
        transaction = transactionRepository.save(transaction);
        
        mockMvc.perform(get("/api/transactions/" + transaction.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference", is("TXN1234567890")));
    }
    
    @Test
    void testGetTransactionById_NotFound() throws Exception {
        mockMvc.perform(get("/api/transactions/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testGetTransactionsByAccount() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setReference("TXN1234567890");
        transaction.setMontant(new BigDecimal("100.00"));
        transaction.setTypeTransaction(Transaction.TypeTransaction.DEPOT);
        transaction.setCompteSource(testAccount);
        transaction.setStatut(Transaction.StatutTransaction.EFFECTUEE);
        transactionRepository.save(transaction);
        
        mockMvc.perform(get("/api/transactions/account/" + testAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].reference", is("TXN1234567890")));
    }
}