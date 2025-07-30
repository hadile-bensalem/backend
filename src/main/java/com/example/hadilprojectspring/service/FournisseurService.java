package com.example.hadilprojectspring.service;
import com.example.hadilprojectspring.dto.FournisseurRequestDto;
import com.example.hadilprojectspring.dto.FournisseurResponseDto;
import com.example.hadilprojectspring.entity.Fournisseur;
import com.example.hadilprojectspring.exception.ResourceNotFoundException;
import com.example.hadilprojectspring.exception.DuplicateResourceException;
import com.example.hadilprojectspring.repository.FournisseurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FournisseurService {
    private  final FournisseurRepository fournisseurRepository;

    // Créer un fournisseur
    public FournisseurResponseDto createFournisseur(FournisseurRequestDto requestDto) {
        log.info("Création d'un nouveau fournisseur avec matricule: {}", requestDto.getMatricule());

        // Vérifier l'unicité du matricule
        if (fournisseurRepository.existsByMatricule(requestDto.getMatricule())) {
            throw new DuplicateResourceException("Un fournisseur avec ce matricule existe déjà");
        }

        // Vérifier l'unicité de l'email
        if (fournisseurRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateResourceException("Un fournisseur avec cet email existe déjà");
        }

        Fournisseur fournisseur = mapToEntity(requestDto);
        Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);

        log.info("Fournisseur créé avec succès, ID: {}", savedFournisseur.getId());
        return mapToResponseDto(savedFournisseur);
    }

    // Obtenir tous les fournisseurs avec pagination
    @Transactional(readOnly = true)
    public Page<FournisseurResponseDto> getAllFournisseurs(int page, int size, String sortBy, String sortDir) {
        log.info("Récupération des fournisseurs - Page: {}, Taille: {}", page, size);

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Fournisseur> fournisseurs = fournisseurRepository.findAll(pageable);

        return fournisseurs.map(this::mapToResponseDto);
    }

    // Obtenir un fournisseur par ID
    @Transactional(readOnly = true)
    public FournisseurResponseDto getFournisseurById(Long id) {
        log.info("Récupération du fournisseur avec ID: {}", id);

        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + id));

        return mapToResponseDto(fournisseur);
    }

    // Mettre à jour un fournisseur
    public FournisseurResponseDto updateFournisseur(Long id, FournisseurRequestDto requestDto) {
        log.info("Mise à jour du fournisseur avec ID: {}", id);

        Fournisseur existingFournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + id));

        // Vérifier l'unicité du matricule (excluant le fournisseur actuel)
        if (fournisseurRepository.existsByMatriculeAndIdNot(requestDto.getMatricule(), id)) {
            throw new DuplicateResourceException("Un autre fournisseur avec ce matricule existe déjà");
        }

        // Vérifier l'unicité de l'email (excluant le fournisseur actuel)
        if (fournisseurRepository.existsByEmailAndIdNot(requestDto.getEmail(), id)) {
            throw new DuplicateResourceException("Un autre fournisseur avec cet email existe déjà");
        }

        updateFournisseurFields(existingFournisseur, requestDto);
        Fournisseur updatedFournisseur = fournisseurRepository.save(existingFournisseur);

        log.info("Fournisseur mis à jour avec succès, ID: {}", id);
        return mapToResponseDto(updatedFournisseur);
    }

    // Supprimer un fournisseur
    public void deleteFournisseur(Long id) {
        log.info("Suppression du fournisseur avec ID: {}", id);

        if (!fournisseurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + id);
        }

        fournisseurRepository.deleteById(id);
        log.info("Fournisseur supprimé avec succès, ID: {}", id);
    }

    // Désactiver/Activer un fournisseur
    public FournisseurResponseDto toggleFournisseurStatus(Long id) {
        log.info("Changement du statut du fournisseur avec ID: {}", id);

        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur non trouvé avec l'ID: " + id));

        fournisseur.setActif(!fournisseur.getActif());
        Fournisseur updatedFournisseur = fournisseurRepository.save(fournisseur);

        log.info("Statut du fournisseur changé: {}, ID: {}", updatedFournisseur.getActif(), id);
        return mapToResponseDto(updatedFournisseur);
    }

    // Recherche par critères
    @Transactional(readOnly = true)
    public Page<FournisseurResponseDto> searchFournisseurs(
            String matricule, String raisonSociale, String email, Boolean actif,
            int page, int size, String sortBy, String sortDir) {

        log.info("Recherche de fournisseurs avec critères");

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Fournisseur> fournisseurs = fournisseurRepository.findByCriteria(
                matricule, raisonSociale, email, actif, pageable);

        return fournisseurs.map(this::mapToResponseDto);
    }

    // Recherche globale
    @Transactional(readOnly = true)
    public Page<FournisseurResponseDto> globalSearch(String searchTerm, int page, int size) {
        log.info("Recherche globale avec terme: {}", searchTerm);

        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreation").descending());
        Page<Fournisseur> fournisseurs = fournisseurRepository.findByGlobalSearch(searchTerm, pageable);

        return fournisseurs.map(this::mapToResponseDto);
    }

    // Obtenir les fournisseurs actifs
    @Transactional(readOnly = true)
    public List<FournisseurResponseDto> getActiveFournisseurs() {
        log.info("Récupération des fournisseurs actifs");

        List<Fournisseur> activeFournisseurs = fournisseurRepository.findByActifTrue();
        return activeFournisseurs.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // Méthodes utilitaires de mapping
    private Fournisseur mapToEntity(FournisseurRequestDto requestDto) {
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setMatricule(requestDto.getMatricule());
        fournisseur.setRaisonSociale(requestDto.getRaisonSociale());
        fournisseur.setAdresse(requestDto.getAdresse());
        fournisseur.setCodeTva(requestDto.getCodeTva());
        fournisseur.setEmail(requestDto.getEmail());
        fournisseur.setTelephone1(requestDto.getTelephone1());
        fournisseur.setTelephone2(requestDto.getTelephone2());
        fournisseur.setFax(requestDto.getFax());
        fournisseur.setResponsableContact(requestDto.getResponsableContact());
        fournisseur.setDevise(requestDto.getDevise());
        fournisseur.setObservations(requestDto.getObservations());
        fournisseur.setActif(requestDto.getActif());
        return fournisseur;
    }

    private FournisseurResponseDto mapToResponseDto(Fournisseur fournisseur) {
        FournisseurResponseDto responseDto = new FournisseurResponseDto();
        responseDto.setId(fournisseur.getId());
        responseDto.setMatricule(fournisseur.getMatricule());
        responseDto.setRaisonSociale(fournisseur.getRaisonSociale());
        responseDto.setAdresse(fournisseur.getAdresse());
        responseDto.setCodeTva(fournisseur.getCodeTva());
        responseDto.setEmail(fournisseur.getEmail());
        responseDto.setTelephone1(fournisseur.getTelephone1());
        responseDto.setTelephone2(fournisseur.getTelephone2());
        responseDto.setFax(fournisseur.getFax());
        responseDto.setResponsableContact(fournisseur.getResponsableContact());
        responseDto.setDevise(fournisseur.getDevise());
        responseDto.setObservations(fournisseur.getObservations());
        responseDto.setActif(fournisseur.getActif());
        responseDto.setDateCreation(fournisseur.getDateCreation());
        responseDto.setDateModification(fournisseur.getDateModification());
        return responseDto;
    }

    private void updateFournisseurFields(Fournisseur existingFournisseur, FournisseurRequestDto requestDto) {
        existingFournisseur.setMatricule(requestDto.getMatricule());
        existingFournisseur.setRaisonSociale(requestDto.getRaisonSociale());
        existingFournisseur.setAdresse(requestDto.getAdresse());
        existingFournisseur.setCodeTva(requestDto.getCodeTva());
        existingFournisseur.setEmail(requestDto.getEmail());
        existingFournisseur.setTelephone1(requestDto.getTelephone1());
        existingFournisseur.setTelephone2(requestDto.getTelephone2());
        existingFournisseur.setFax(requestDto.getFax());
        existingFournisseur.setResponsableContact(requestDto.getResponsableContact());
        existingFournisseur.setDevise(requestDto.getDevise());
        existingFournisseur.setObservations(requestDto.getObservations());
        existingFournisseur.setActif(requestDto.getActif());
    }
}
