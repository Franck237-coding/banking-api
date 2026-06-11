package com.banking;

import com.banking.model.*;
import com.banking.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Tests d'Intégration API - Swagger")
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Bank bank;
    private User user;
    private Account account;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        userRepository.deleteAll();
        bankRepository.deleteAll();

        bank = new Bank();
        bank.setNom("Banque Nationale");
        bank.setCode("BNK001");
        bank.setAdresse("123 Rue Principale");
        bank.setTelephone("0123456789");
        bank = bankRepository.save(bank);

        user = new User();
        user.setNom("Dupont");
        user.setPrenom("Jean");
        user.setEmail("jean@test.com");
        user.setTelephone("0612345678");
        user.setBank(bank);
        user = userRepository.save(user);

        account = new Account();
        account.setNumeroCompte("BANK123456789");
        account.setSolde(new BigDecimal("1000.00"));
        account.setTypeCompte(Account.TypeCompte.COURANT);
        account.setUser(user);
        account.setBank(bank);
        account = accountRepository.save(account);
    }

    // ===============================
    // TESTS BANK API
    // ===============================

    @Test
    @DisplayName("IT-01: Créer une banque via Swagger")
    void testCreerBanque() throws Exception {
        mockMvc.perform(post("/api/banks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nom\":\"Banque Test\",\"code\":\"BNK002\",\"adresse\":\"456 Test\",\"telephone\":\"0987654321\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom", is("Banque Test")))
                .andExpect(jsonPath("$.code", is("BNK002")));
    }

    @Test
    @DisplayName("IT-02: Lister les banques via Swagger")
    void testListerBanques() throws Exception {
        mockMvc.perform(get("/api/banks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ===============================
    // TESTS USER API
    // ===============================

    @Test
    @DisplayName("IT-03: Créer un utilisateur via Swagger")
    void testCreerUtilisateur() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nom\":\"Martin\",\"prenom\":\"Sophie\",\"email\":\"sophie@test.com\",\"telephone\":\"0699999999\",\"bankId\":" + bank.getId() + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom", is("Martin")))
                .andExpect(jsonPath("$.prenom", is("Sophie")));
    }

    @Test
    @DisplayName("IT-04: Lister les utilisateurs via Swagger")
    void testListerUtilisateurs() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ===============================
    // TESTS ACCOUNT API
    // ===============================

    @Test
    @DisplayName("IT-05: Créer un compte avec banque via Swagger")
    void testCreerCompte() throws Exception {
        mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"numeroCompte\":\"BANK987654321\",\"solde\":500.00,\"typeCompte\":\"COURANT\",\"userId\":" + user.getId() + ",\"bankId\":" + bank.getId() + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCompte", is("BANK987654321")))
                .andExpect(jsonPath("$.solde", is(500.0)));
    }

    @Test
    @DisplayName("IT-06: Lister les comptes via Swagger")
    void testListerComptes() throws Exception {
        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    // ===============================
    // TESTS TRANSACTION API - DEPOT/RETRAIT
    // ===============================

    @Test
    @DisplayName("IT-07: Effectuer un dépôt via Swagger")
    void testDepot() throws Exception {
        mockMvc.perform(post("/api/transactions/deposit")
                .param("numeroCompte", "BANK123456789")
                .param("montant", "500.00")
                .param("description", "Salaire"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeTransaction", is("DEPOT")))
                .andExpect(jsonPath("$.statut", is("EFFECTUEE")));
    }

    @Test
    @DisplayName("IT-08: Effectuer un retrait via Swagger")
    void testRetrait() throws Exception {
        mockMvc.perform(post("/api/transactions/withdraw")
                .param("numeroCompte", "BANK123456789")
                .param("montant", "300.00")
                .param("description", "Retrait espèces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.typeTransaction", is("RETRAIT")))
                .andExpect(jsonPath("$.statut", is("EFFECTUEE")));
    }

    @Test
    @DisplayName("IT-09: Retrait avec solde insuffisant via Swagger")
    void testRetraitSoldeInsuffisant() throws Exception {
        mockMvc.perform(post("/api/transactions/withdraw")
                .param("numeroCompte", "BANK123456789")
                .param("montant", "5000.00")
                .param("description", "Test"))
                .andExpect(status().isBadRequest());
    }
}