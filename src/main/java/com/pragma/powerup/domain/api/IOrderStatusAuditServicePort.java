package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderStatusAuditServicePort {

    OrderStatusAuditModel createAudit(OrderStatusAuditModel auditModel);

    Page<OrderStatusAuditModel> getAuditHistory(Long clientId, Long orderId, List<String> actionTypes, Pageable pageable);
}

