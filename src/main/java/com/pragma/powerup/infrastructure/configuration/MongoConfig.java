package com.pragma.powerup.infrastructure.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.pragma.powerup.infrastructure.out.mongodb.repository")
@EnableMongoAuditing
public class MongoConfig {
}

