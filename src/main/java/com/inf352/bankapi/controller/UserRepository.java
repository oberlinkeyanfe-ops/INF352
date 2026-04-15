package com.inf352.bankapi.controller;

import java.util.List;

import com.inf352.bankapi.model.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<BankUser, Long> {

    default BankUser addUser(BankUser user) {
        return save(user);
    }

    default List<BankUser> selectUsers() {
        return findAll();
    }
}