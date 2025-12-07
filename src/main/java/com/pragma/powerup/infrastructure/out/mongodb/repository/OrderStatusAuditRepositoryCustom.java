package com.pragma.powerup.infrastructure.out.mongodb.repository;

import com.pragma.powerup.infrastructure.out.mongodb.document.OrderStatusAuditDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderStatusAuditRepositoryCustom {

    Page<OrderStatusAuditDocument> findByFilters
            (
            Long clientId,
            Long orderId,
            List<String> actionTypes,
            Pageable pageable
            );
}

