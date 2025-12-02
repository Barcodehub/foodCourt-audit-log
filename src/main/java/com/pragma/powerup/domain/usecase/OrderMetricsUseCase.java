package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderMetricsServicePort;
import com.pragma.powerup.domain.api.IOrderStatusAuditServicePort;
import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import com.pragma.powerup.domain.spi.IOrderMetricsPersistencePort;
import com.pragma.powerup.domain.spi.IOrderStatusAuditPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class OrderMetricsUseCase implements IOrderMetricsServicePort {

    private final IOrderMetricsPersistencePort auditPersistencePort;


}

