package com.example.hadilprojectspring.entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "fournisseurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le matricule est obligatoire")
    @Size(max = 50, message = "Le matricule ne doit pas dépasser 50 caractères")
    @Column(unique = true, nullable = false)
    private String matricule;

    @NotBlank(message = "La raison sociale est obligatoire")
    @Size(max = 200, message = "La raison sociale ne doit pas dépasser 200 caractères")
    @Column(name = "raison_sociale", nullable = false)
    private String raisonSociale;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 500, message = "L'adresse ne doit pas dépasser 500 caractères")
    @Column(nullable = false)
    private String adresse;

    @NotBlank(message = "Le code TVA est obligatoire")
    @Size(max = 20, message = "Le code TVA ne doit pas dépasser 20 caractères")
    @Column(name = "code_tva", nullable = false)
    private String codeTva;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Le téléphone 1 est obligatoire")
    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir 8 chiffres")
    @Column(name = "telephone_1", nullable = false)
    private String telephone1;

    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir 8 chiffres")
    @Column(name = "telephone_2")
    private String telephone2;

    @Pattern(regexp = "^[0-9]{8}$", message = "Le fax doit contenir 8 chiffres")
    private String fax;

    @NotBlank(message = "Le responsable à contacter est obligatoire")
    @Size(max = 100, message = "Le nom du responsable ne doit pas dépasser 100 caractères")
    @Column(name = "responsable_contact", nullable = false)
    private String responsableContact;

    @NotBlank(message = "La devise est obligatoire")
    @Size(max = 10, message = "La devise ne doit pas dépasser 10 caractères")
    private String devise;

    @Size(max = 1000, message = "Les observations ne doivent pas dépasser 1000 caractères")
    private String observations;

    @Column(name = "actif", nullable = false)
    private Boolean actif = true;

    @CreationTimestamp
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @UpdateTimestamp
    @Column(name = "date_modification")
    private LocalDateTime dateModification;
}
