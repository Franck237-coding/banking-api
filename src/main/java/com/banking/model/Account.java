package com.banking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts")
@JsonIgnoreProperties({"transactionsSource", "transactionsDestination", "hibernateLazyInitializer", "handler"})
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le numéro de compte est obligatoire")
    @Column(unique = true, nullable = false)
    private String numeroCompte;
    
    @NotNull(message = "Le solde est obligatoire")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal solde = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCompte typeCompte = TypeCompte.COURANT;
    
    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"accounts", "hibernateLazyInitializer", "handler"})
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    @JsonIgnoreProperties({"users", "hibernateLazyInitializer", "handler"})
    private Bank bank;
    
    @OneToMany(mappedBy = "compteSource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactionsSource;
    
    @OneToMany(mappedBy = "compteDestination", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactionsDestination;
    
    public enum TypeCompte {
        COURANT, EPARGNE, LIVRET_A
    }
    
    public Account() {}
    
    public Account(String numeroCompte, User user) {
        this.numeroCompte = numeroCompte;
        this.user = user;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumeroCompte() { return numeroCompte; }
    public void setNumeroCompte(String numeroCompte) { this.numeroCompte = numeroCompte; }
    
    public BigDecimal getSolde() { return solde; }
    public void setSolde(BigDecimal solde) { this.solde = solde; }
    
    public TypeCompte getTypeCompte() { return typeCompte; }
    public void setTypeCompte(TypeCompte typeCompte) { this.typeCompte = typeCompte; }
    
    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Bank getBank() { return bank; }
    public void setBank(Bank bank) { this.bank = bank; }
    
    public List<Transaction> getTransactionsSource() { return transactionsSource; }
    public void setTransactionsSource(List<Transaction> transactionsSource) { this.transactionsSource = transactionsSource; }
    
    public List<Transaction> getTransactionsDestination() { return transactionsDestination; }
    public void setTransactionsDestination(List<Transaction> transactionsDestination) { this.transactionsDestination = transactionsDestination; }
}