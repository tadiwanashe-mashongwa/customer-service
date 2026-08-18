package com.example.customerservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    OpenAPI customerServiceOpenApi() {
        return new OpenAPI().info(new Info()
                .title("SpareLink Customer Service API")
                .version("v1")
                .description("Customer profiles, delivery addresses, and saved vehicles."));
    }
}
