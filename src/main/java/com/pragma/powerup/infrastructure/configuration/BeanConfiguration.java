package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IOrderMetricsServicePort;
import com.pragma.powerup.domain.api.IOrderStatusAuditServicePort;
import com.pragma.powerup.domain.spi.IOrderMetricsPersistencePort;
import com.pragma.powerup.domain.spi.IOrderStatusAuditPersistencePort;
import com.pragma.powerup.domain.usecase.OrderMetricsUseCase;
import com.pragma.powerup.domain.usecase.OrderStatusAuditUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de beans del dominio
 * Responsabilidad: Configurar e inyectar los casos de uso del dominio
 * Sigue el principio de inversión de dependencias (SOLID)
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public IOrderStatusAuditServicePort orderStatusAuditServicePort(
            IOrderStatusAuditPersistencePort auditPersistencePort) {
        return new OrderStatusAuditUseCase(auditPersistencePort);
    }

    @Bean
    public IOrderMetricsServicePort orderMetricsServicePort(
            IOrderMetricsPersistencePort metricsPersistencePort) {
        return new OrderMetricsUseCase(metricsPersistencePort);
    }
}

