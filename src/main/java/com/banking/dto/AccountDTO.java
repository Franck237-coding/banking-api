package com.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AccountDTO {
    
    @NotBlank(message = "Le numéro de compte est obligatoire")
    private String numeroCompte;
    
    @NotNull(message = "Le solde est obligatoire")
    private BigDecimal solde = BigDecimal.ZERO;
    
    @NotBlank(message = "Le type de compte est obligatoire")
    private String typeCompte = "COURANT";
    
    @NotNull(message = "L'ID utilisateur est obligatoire")
    private Long userId;
    
    public AccountDTO() {}
    
    public AccountDTO(String numeroCompte, BigDecimal solde, String typeCompte, Long userId) {
        this.numeroCompte = numeroCompte;
        this.solde = solde;
        this.typeCompte = typeCompte;
        this.userId = userId;
    }
    
    // Getters and Setters
    public String getNumeroCompte() { return numeroCompte; }
    public void setNumeroCompte(String numeroCompte) { this.numeroCompte = numeroCompte; }
    
    public BigDecimal getSolde() { return solde; }
    public void setSolde(BigDecimal solde) { this.solde = solde; }
    
    public String getTypeCompte() { return typeCompte; }
    public void setTypeCompte(String typeCompte) { this.typeCompte = typeCompte; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
