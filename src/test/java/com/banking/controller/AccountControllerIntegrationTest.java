package com.banking.controller;

import com.banking.model.Account;
import com.banking.model.User;
import com.banking.repository.AccountRepository;
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
class AccountControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    private User testUser;
    private Account testAccount;
    
    @BeforeEach
    void setUp() {
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
    }
    
    @Test
    void testCreateAccount_Success() throws Exception {
        String accountJson = objectMapper.writeValueAsString(testAccount);
        
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCompte", is("BANK1234567890")))
                .andExpect(jsonPath("$.solde", is(1000.00)));
    }
    
    @Test
    void testCreateAccount_NumeroAlreadyExists() throws Exception {
        accountRepository.save(testAccount);
        
        String accountJson = objectMapper.writeValueAsString(testAccount);
        
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJson))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testGetAllAccounts() throws Exception {
        accountRepository.save(testAccount);
        
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].numeroCompte", is("BANK1234567890")));
    }
    
    @Test
    void testGetAccountById_Found() throws Exception {
        Account savedAccount = accountRepository.save(testAccount);
        
        mockMvc.perform(get("/api/accounts/" + savedAccount.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCompte", is("BANK1234567890")));
    }
    
    @Test
    void testGetAccountById_NotFound() throws Exception {
        mockMvc.perform(get("/api/accounts/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testUpdateAccount_Success() throws Exception {
        Account savedAccount = accountRepository.save(testAccount);
        
        savedAccount.setTypeCompte(Account.TypeCompte.EPARGNE);
        String accountJson = objectMapper.writeValueAsString(savedAccount);
        
        mockMvc.perform(put("/api/accounts/" + savedAccount.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(accountJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeCompte", is("EPARGNE")));
    }
    
    @Test
    void testDeleteAccount_Success() throws Exception {
        Account savedAccount = accountRepository.save(testAccount);
        
        mockMvc.perform(delete("/api/accounts/" + savedAccount.getId()))
                .andExpect(status().isNoContent());
        
        assertTrue(accountRepository.findById(savedAccount.getId()).isEmpty());
    }
    
    @Test
    void testGetAccountsByUser() throws Exception {
        accountRepository.save(testAccount);
        
        mockMvc.perform(get("/api/accounts/user/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].numeroCompte", is("BANK1234567890")));
    }
}