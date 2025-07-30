package com.example.hadilprojectspring.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FournisseurResponseDto {
    private Long id;
    private String matricule;
    private String raisonSociale;
    private String adresse;
    private String codeTva;
    private String email;
    private String telephone1;
    private String telephone2;
    private String fax;
    private String responsableContact;
    private String devise;
    private String observations;
    private Boolean actif;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
}
