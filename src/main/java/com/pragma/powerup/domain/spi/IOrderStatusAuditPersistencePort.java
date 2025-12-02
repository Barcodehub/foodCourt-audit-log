package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderStatusAuditPersistencePort {

    OrderStatusAuditModel saveAudit(OrderStatusAuditModel auditModel);

    Page<OrderStatusAuditModel> findByFilters(Long clientId, Long orderId, List<String> actionTypes, Pageable pageable);

    Page<OrderStatusAuditModel> findByClientId(Long clientId, Pageable pageable);
}

