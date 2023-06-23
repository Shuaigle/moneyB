package com.money.money.conf;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class BaseIntegrationTests {

    @Container
    private final static PostgreSQLContainer<?> postgreSQLContainer;

    static {
        try (PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")) {
            postgreSQLContainer = container
                    .withDatabaseName("money-integration-tests-db")
                    .withUsername("postgres")
                    .withPassword("password");
        }
    }

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }
}
