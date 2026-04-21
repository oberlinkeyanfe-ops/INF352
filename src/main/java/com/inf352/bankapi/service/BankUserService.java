package com.inf352.bankapi.service;

import java.util.List;

import com.inf352.bankapi.controller.UserRepository;
import com.inf352.bankapi.exception.DuplicateResourceException;
import com.inf352.bankapi.exception.ResourceNotFoundException;
import com.inf352.bankapi.model.BankAccount;
import com.inf352.bankapi.model.BankUser;
import com.inf352.bankapi.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankUserService {

    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public BankUserService(UserRepository userRepository, BankAccountRepository bankAccountRepository) {
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankUser createUser(BankUser user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Un utilisateur avec cet email existe deja");
        }

        BankAccount primaryAccount = new BankAccount();
        primaryAccount.setAccountNumber(user.getAccountNumber());
        primaryAccount.setUser(user);
        user.getAccounts().add(primaryAccount);

        return userRepository.save(user);
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
