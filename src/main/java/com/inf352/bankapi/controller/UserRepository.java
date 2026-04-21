package com.inf352.bankapi.controller;

import java.util.List;
import java.util.Optional;

import com.inf352.bankapi.model.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<BankUser, Long> {

    Optional<BankUser> findByEmail(String email);

    Optional<BankUser> findByApiToken(String apiToken);

    boolean existsByEmail(String email);

    default BankUser addUser(BankUser user) {
        return save(user);
    }

    default List<BankUser> selectUsers() {
        return findAll();
    }
}