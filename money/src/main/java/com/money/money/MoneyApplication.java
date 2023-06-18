package com.money.money;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "MoneyB API",
                version = "v1",
                description = "CRUD of money diary records.",
                contact = @Contact(
                        name = "Shuaigle",
                        email = "layard3@gmail.com",
                        url = "https://github.com/Shuaigle"
                )
        )
)
public class MoneyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyApplication.class, args);
        SpringDocUtils.getConfig().replaceWithClass(
                org.springframework.data.domain.Pageable.class, PageableAsQueryParam.class);

    }

}
