package com.pragma.powerup.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Configuración de MongoDB
 * Habilita:
 * - Repositorios de MongoDB en el paquete especificado
 * - Auditoría automática (@CreatedDate, @LastModifiedDate, etc.)
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.pragma.powerup.infrastructure.out.mongodb.repository")
@EnableMongoAuditing
public class MongoConfig {
}

