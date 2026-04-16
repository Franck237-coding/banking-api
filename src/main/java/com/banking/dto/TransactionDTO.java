package com.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class TransactionDTO {
    
    @NotBlank(message = "Le numéro de compte source est obligatoire")
    private String compteSource;
    
    private String compteDestination;
    
    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal montant;
    
    @NotBlank(message = "Le type de transaction est obligatoire")
    private String typeTransaction;
    
    private String description;
    
    public TransactionDTO() {}
    
    public TransactionDTO(String compteSource, String compteDestination, BigDecimal montant, String typeTransaction, String description) {
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;
        this.montant = montant;
        this.typeTransaction = typeTransaction;
        this.description = description;
    }
    
    // Getters and Setters
    public String getCompteSource() { return compteSource; }
    public void setCompteSource(String compteSource) { this.compteSource = compteSource; }
    
    public String getCompteDestination() { return compteDestination; }
    public void setCompteDestination(String compteDestination) { this.compteDestination = compteDestination; }
    
    public BigDecimal getMontant() { return montant; }
    public void setMontant(BigDecimal montant) { this.montant = montant; }
    
    public String getTypeTransaction() { return typeTransaction; }
    public void setTypeTransaction(String typeTransaction) { this.typeTransaction = typeTransaction; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
