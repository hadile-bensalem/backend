package com.example.hadilprojectspring.controller;


import com.example.hadilprojectspring.dto.ApiResponse;
import com.example.hadilprojectspring.dto.FournisseurRequestDto;
import com.example.hadilprojectspring.dto.FournisseurResponseDto;
import com.example.hadilprojectspring.service.FournisseurService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/fournisseurs")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FournisseurController {
    private final FournisseurService fournisseurService;

    @PostMapping
    public ResponseEntity<ApiResponse<FournisseurResponseDto>> createFournisseur(
            @Valid @RequestBody FournisseurRequestDto requestDto) {

        log.info("Requête de création de fournisseur reçue");

        FournisseurResponseDto createdFournisseur = fournisseurService.createFournisseur(requestDto);

        ApiResponse<FournisseurResponseDto> response = ApiResponse.success(
                "Fournisseur créé avec succès",
                createdFournisseur
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<FournisseurResponseDto>>> getAllFournisseurs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreation") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("Requête de récupération de tous les fournisseurs");

        Page<FournisseurResponseDto> fournisseurs = fournisseurService.getAllFournisseurs(page, size, sortBy, sortDir);

        ApiResponse<Page<FournisseurResponseDto>> response = ApiResponse.success(
                "Fournisseurs récupérés avec succès",
                fournisseurs
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FournisseurResponseDto>> getFournisseurById(@PathVariable Long id) {
        log.info("Requête de récupération du fournisseur avec ID: {}", id);

        FournisseurResponseDto fournisseur = fournisseurService.getFournisseurById(id);

        ApiResponse<FournisseurResponseDto> response = ApiResponse.success(
                "Fournisseur récupéré avec succès",
                fournisseur
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FournisseurResponseDto>> updateFournisseur(
            @PathVariable Long id,
            @Valid @RequestBody FournisseurRequestDto requestDto) {

        log.info("Requête de mise à jour du fournisseur avec ID: {}", id);

        FournisseurResponseDto updatedFournisseur = fournisseurService.updateFournisseur(id, requestDto);

        ApiResponse<FournisseurResponseDto> response = ApiResponse.success(
                "Fournisseur mis à jour avec succès",
                updatedFournisseur
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFournisseur(@PathVariable Long id) {
        log.info("Requête de suppression du fournisseur avec ID: {}", id);

        fournisseurService.deleteFournisseur(id);

        ApiResponse<Void> response = ApiResponse.success("Fournisseur supprimé avec succès");

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<FournisseurResponseDto>> toggleFournisseurStatus(@PathVariable Long id) {
        log.info("Requête de changement de statut du fournisseur avec ID: {}", id);

        FournisseurResponseDto updatedFournisseur = fournisseurService.toggleFournisseurStatus(id);

        ApiResponse<FournisseurResponseDto> response = ApiResponse.success(
                "Statut du fournisseur modifié avec succès",
                updatedFournisseur
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<FournisseurResponseDto>>> searchFournisseurs(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String raisonSociale,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean actif,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreation") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        log.info("Requête de recherche de fournisseurs par critères");

        Page<FournisseurResponseDto> fournisseurs = fournisseurService.searchFournisseurs(
                matricule, raisonSociale, email, actif, page, size, sortBy, sortDir);

        ApiResponse<Page<FournisseurResponseDto>> response = ApiResponse.success(
                "Recherche effectuée avec succès",
                fournisseurs
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/global")
    public ResponseEntity<ApiResponse<Page<FournisseurResponseDto>>> globalSearch(
            @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Requête de recherche globale avec terme: {}", searchTerm);

        Page<FournisseurResponseDto> fournisseurs = fournisseurService.globalSearch(searchTerm, page, size);

        ApiResponse<Page<FournisseurResponseDto>> response = ApiResponse.success(
                "Recherche globale effectuée avec succès",
                fournisseurs
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<FournisseurResponseDto>>> getActiveFournisseurs() {
        log.info("Requête de récupération des fournisseurs actifs");

        List<FournisseurResponseDto> activeFournisseurs = fournisseurService.getActiveFournisseurs();

        ApiResponse<List<FournisseurResponseDto>> response = ApiResponse.success(
                "Fournisseurs actifs récupérés avec succès",
                activeFournisseurs
        );

        return ResponseEntity.ok(response);
    }
}
