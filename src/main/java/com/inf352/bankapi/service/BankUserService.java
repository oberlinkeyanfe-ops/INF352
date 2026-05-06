package com.inf352.bankapi.service;

import java.util.List;

import com.inf352.bankapi.controller.UserRepository;
import com.inf352.bankapi.exception.DuplicateResourceException;
import com.inf352.bankapi.exception.InvalidVerificationCodeException;
import com.inf352.bankapi.exception.ResourceNotFoundException;
import com.inf352.bankapi.model.BankUser;
import com.inf352.bankapi.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Service
@Transactional
public class BankUserService {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final EmailService emailService;

    public BankUserService(UserRepository userRepository, BankAccountRepository bankAccountRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.emailService = emailService;
    }

    public BankUser createUser(BankUser user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Un utilisateur avec cet email existe deja");
        }

        String verificationCode = generateVerificationCode();
        user.setVerificationCode(verificationCode);
        user.setVerified(false);

        BankUser savedUser = userRepository.save(user);
        emailService.sendVerificationEmail(savedUser.getEmail(), verificationCode);

        return savedUser;
    }

    public BankUser verifyUser(String email, String code) {
        BankUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        if (user.isVerified()) {
            return user; // Already verified
        }

        if (user.getVerificationCode() != null && user.getVerificationCode().equals(code)) {
            user.setVerified(true);
            user.setVerificationCode(null); // Code is used, invalidate it
            return userRepository.save(user);
        } else {
            throw new InvalidVerificationCodeException("Code de vérification invalide");
        }
    }
    
    private String generateVerificationCode() {
        return String.format("%04d", new SecureRandom().nextInt(10000));
    }

    public List<BankUser> listUsers() {
        return userRepository.findAll();
    }

    public BankUser getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
    }

    public BankUser updateUser(Long id, BankUser incomingUser) {
        BankUser existingUser = getUser(id);
        if (!existingUser.getEmail().equalsIgnoreCase(incomingUser.getEmail())
                && userRepository.existsByEmail(incomingUser.getEmail())) {
            throw new DuplicateResourceException("Un utilisateur avec cet email existe deja");
        }

        existingUser.setFirstName(incomingUser.getFirstName());
        existingUser.setLastName(incomingUser.getLastName());
        existingUser.setEmail(incomingUser.getEmail());
        existingUser.setPhone(incomingUser.getPhone());
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Utilisateur introuvable");
        }
        userRepository.deleteById(id);
    }

    public String issueToken(String email, String phone) {
        BankUser user = userRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        if (!user.getPhone().trim().equals(phone.trim())) {
            throw new ResourceNotFoundException("Utilisateur introuvable");
        }
        return user.getApiToken();
    }

    public BankUser authenticateByToken(String token) {
        return userRepository.findByApiToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token invalide"));
    }

}
