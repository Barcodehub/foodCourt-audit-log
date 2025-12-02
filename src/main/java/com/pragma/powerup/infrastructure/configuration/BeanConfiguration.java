package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IOrderStatusAuditServicePort;
import com.pragma.powerup.domain.spi.IOrderStatusAuditPersistencePort;
import com.pragma.powerup.domain.usecase.OrderStatusAuditUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IOrderStatusAuditServicePort orderStatusAuditServicePort(
            IOrderStatusAuditPersistencePort auditPersistencePort) {
        return new OrderStatusAuditUseCase(auditPersistencePort);
    }
}

