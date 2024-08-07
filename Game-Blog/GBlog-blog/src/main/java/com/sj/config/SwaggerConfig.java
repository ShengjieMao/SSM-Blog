package com.sj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sj.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("SJM", "https://www.placeholder.com",
                "phone placeholder" +
                "email@email.placeholder");
        return new ApiInfoBuilder()
                .title("Doc Title")
                .description("Doc Description")
                .contact(contact)   // Contact
                .version("1.1.0")  // Version
                .build();
    }
}