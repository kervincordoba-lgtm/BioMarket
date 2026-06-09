package com.biomarket.demo.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "BioMarket API",
                version = "1.0.0",
                description = "Documentacion OpenAPI para productos, inventario y pedidos de BioMarket.",
                contact = @Contact(name = "Kervin Cordoba", email = "kervin.cordoba@udea.edu.co"),
                license = @License(name = "Proyecto academico")),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor local")
        })
public class OpenApiConfig {
}
