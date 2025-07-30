package com.example.hadilprojectspring.service;
import com.example.hadilprojectspring.dto.JwtResponse;
import com.example.hadilprojectspring.dto.LoginRequest;
import com.example.hadilprojectspring.dto.SignupRequest;
import com.example.hadilprojectspring.entity.User;
import com.example.hadilprojectspring.repository.UserRepository;
import com.example.hadilprojectspring.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Inscription d'un nouvel utilisateur
     */
    public String signup(SignupRequest signupRequest) {
        // Vérifier si le nom d'utilisateur existe déjà
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Erreur: Le nom d'utilisateur est déjà pris!");
        }

        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new RuntimeException("Erreur: L'email est déjà utilisé!");
        }

        // Créer un nouvel utilisateur
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));

        // Sauvegarder l'utilisateur
        userRepository.save(user);

        return "Utilisateur enregistré avec succès!";
    }

    /**
     * Connexion d'un utilisateur
     */
    public JwtResponse login(LoginRequest loginRequest) {
        try {
            // Authentifier l'utilisateur
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(),
                            loginRequest.getPassword())
            );

            // Définir l'authentification dans le contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Récupérer les détails de l'utilisateur (UserDetails)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Générer le token JWT
            String jwt = jwtUtils.generateJwtToken(userDetails);

            // Récupérer l'utilisateur depuis la base de données pour obtenir les détails complets
            User user = userRepository.findByUsernameOrEmail(
                    loginRequest.getUsernameOrEmail(),
                    loginRequest.getUsernameOrEmail()
            ).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            return new JwtResponse(jwt,
                    user.getId(),
                    user.getUsername(),
                    user.getEmail());

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Nom d'utilisateur ou mot de passe incorrect!");
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la connexion: " + e.getMessage());
        }
    }

    /**
     * Récupérer l'utilisateur actuellement connecté
     */
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Récupérer l'utilisateur depuis la base de données
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        }

        throw new RuntimeException("Aucun utilisateur connecté trouvé!");
    }

    /**
     * Vérifier si un nom d'utilisateur est disponible
     */
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * Vérifier si un email est disponible
     */
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}
