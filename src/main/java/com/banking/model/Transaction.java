package com.banking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "La référence est obligatoire")
    @Column(unique = true, nullable = false)
    private String reference;
    
    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTransaction typeTransaction;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime dateTransaction = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutTransaction statut = StatutTransaction.EN_ATTENTE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_source_id")
    private Account compteSource;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compte_destination_id")
    private Account compteDestination;
    
    public enum TypeTransaction {
        DEPOT, RETRAIT, VIREMENT
    }
    
    public enum StatutTransaction {
        EN_ATTENTE, EFFECTUEE, ECHOUEE
    }
    
    public Transaction() {}
    
    public Transaction(String reference, BigDecimal montant, TypeTransaction typeTransaction, String description) {
        this.reference = reference;
        this.montant = montant;
        this.typeTransaction = typeTransaction;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    
    public TypeTransaction getTypeTransaction() { return typeTransaction; }
    public void setTypeTransaction(TypeTransaction typeTransaction) { this.typeTransaction = typeTransaction; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalDateTime getDateTransaction() { return dateTransaction; }
    public void setDateTransaction(LocalDateTime dateTransaction) { this.dateTransaction = dateTransaction; }
    
    public StatutTransaction getStatut() { return statut; }
    public void setStatut(StatutTransaction statut) { this.statut = statut; }
    
    public Account getCompteSource() { return compteSource; }
    public void setCompteSource(Account compteSource) { this.compteSource = compteSource; }
    
    public Account getCompteDestination() { return compteDestination; }
    public void setCompteDestination(Account compteDestination) { this.compteDestination = compteDestination; }
}
