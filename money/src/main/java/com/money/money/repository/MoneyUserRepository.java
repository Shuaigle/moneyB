package com.money.money.repository;

import com.money.money.domain.MoneyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MoneyUserRepository extends JpaRepository<MoneyUser, Long> {
    Optional<MoneyUser> findByEmail(String email);
}

