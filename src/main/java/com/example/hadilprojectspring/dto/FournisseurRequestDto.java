package com.example.hadilprojectspring.dto;


import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FournisseurRequestDto {
    @NotBlank(message = "Le matricule est obligatoire")
    @Size(max = 50, message = "Le matricule ne doit pas dépasser 50 caractères")
    private String matricule;

    @NotBlank(message = "La raison sociale est obligatoire")
    @Size(max = 200, message = "La raison sociale ne doit pas dépasser 200 caractères")
    private String raisonSociale;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 500, message = "L'adresse ne doit pas dépasser 500 caractères")
    private String adresse;

    @NotBlank(message = "Le code TVA est obligatoire")
    @Size(max = 20, message = "Le code TVA ne doit pas dépasser 20 caractères")
    private String codeTva;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le téléphone 1 est obligatoire")
    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir 8 chiffres")
    private String telephone1;

    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir 8 chiffres")
    private String telephone2;

    @Pattern(regexp = "^[0-9]{8}$", message = "Le fax doit contenir 8 chiffres")
    private String fax;

    @NotBlank(message = "Le responsable à contacter est obligatoire")
    @Size(max = 100, message = "Le nom du responsable ne doit pas dépasser 100 caractères")
    private String responsableContact;

    @NotBlank(message = "La devise est obligatoire")
    @Size(max = 10, message = "La devise ne doit pas dépasser 10 caractères")
    private String devise;

    @Size(max = 1000, message = "Les observations ne doivent pas dépasser 1000 caractères")
    private String observations;

    private Boolean actif = true;
}
