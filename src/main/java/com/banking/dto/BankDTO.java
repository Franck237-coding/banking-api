package com.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BankDTO {
    
    @NotBlank(message = "Le nom de la banque est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;
    
    @NotBlank(message = "Le code de la banque est obligatoire")
    @Size(min = 2, max = 20, message = "Le code doit contenir entre 2 et 20 caractères")
    private String code;
    
    @Size(max = 200, message = "L'adresse ne doit pas dépasser 200 caractères")
    private String adresse;
    
    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
    private String telephone;
    
    public BankDTO() {}
    
    public BankDTO(String nom, String code, String adresse, String telephone) {
        this.nom = nom;
        this.code = code;
        this.adresse = adresse;
        this.telephone = telephone;
    }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
}