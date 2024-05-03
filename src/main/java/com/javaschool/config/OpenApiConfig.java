package com.javaschool.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import lombok.Builder;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
@OpenAPIDefinition(info = @Info(title = "REST API",version = "1.1",
        contact = @Contact(name = "DEV",email = "sophak1502@gmail.com")),
        security = {@SecurityRequirement(name = "bearerToken")}

)
@SecuritySchemes({
        @SecurityScheme(name = "bearerToken",type = SecuritySchemeType.HTTP,scheme = "bearer",bearerFormat = "JWT")

})
@Service
@Builder
public class OpenApiConfig {
}
