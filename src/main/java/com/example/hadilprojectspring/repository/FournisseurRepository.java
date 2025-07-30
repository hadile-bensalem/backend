package com.example.hadilprojectspring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.hadilprojectspring.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    // Vérifier l'existence par matricule
    boolean existsByMatricule(String matricule);

    // Vérifier l'existence par email
    boolean existsByEmail(String email);

    // Vérifier l'existence par matricule excluant un ID spécifique (pour la mise à jour)
    boolean existsByMatriculeAndIdNot(String matricule, Long id);

    // Vérifier l'existence par email excluant un ID spécifique (pour la mise à jour)
    boolean existsByEmailAndIdNot(String email, Long id);

    // Trouver par matricule
    Optional<Fournisseur> findByMatricule(String matricule);

    // Trouver tous les fournisseurs actifs
    List<Fournisseur> findByActifTrue();

    // Trouver tous les fournisseurs inactifs
    List<Fournisseur> findByActifFalse();

    // Recherche par critères avec pagination
    @Query("SELECT f FROM Fournisseur f WHERE " +
            "(:matricule IS NULL OR LOWER(f.matricule) LIKE LOWER(CONCAT('%', :matricule, '%'))) AND " +
            "(:raisonSociale IS NULL OR LOWER(f.raisonSociale) LIKE LOWER(CONCAT('%', :raisonSociale, '%'))) AND " +
            "(:email IS NULL OR LOWER(f.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:actif IS NULL OR f.actif = :actif)")
    Page<Fournisseur> findByCriteria(
            @Param("matricule") String matricule,
            @Param("raisonSociale") String raisonSociale,
            @Param("email") String email,
            @Param("actif") Boolean actif,
            Pageable pageable
    );

    // Recherche globale
    @Query("SELECT f FROM Fournisseur f WHERE " +
            "LOWER(f.matricule) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(f.raisonSociale) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(f.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(f.responsableContact) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Fournisseur> findByGlobalSearch(@Param("searchTerm") String searchTerm, Pageable pageable);
}
