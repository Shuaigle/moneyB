package com.money.money.repository;

import com.money.money.conf.BaseIntegrationTests;
import com.money.money.domain.MoneyUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class MoneyUserRepositoryIntegrationTest extends BaseIntegrationTests {

    @Autowired
    MoneyUserRepository moneyUserRepository;

    private MoneyUser user;

    @BeforeEach
    public void setUp() {
        user = MoneyUser.builder()
                .email("test@test.com")
                .username("testUser")
                .password("password")
                .build();

        moneyUserRepository.saveAndFlush(user); // Save and immediately commit to the DB
    }

    @Test
    public void whenFindByEmail_thenReturnMoneyUser() {
        Optional<MoneyUser> foundUser = moneyUserRepository.findByEmail(user.getEmail());

        assertThat(foundUser)
                .isPresent()
                .hasValueSatisfying(dbUser -> {
                    assertThat(dbUser.getEmail()).isEqualTo(user.getEmail());
                    assertThat(dbUser.getUsername()).isEqualTo(user.getUsername());
                });
    }
}

