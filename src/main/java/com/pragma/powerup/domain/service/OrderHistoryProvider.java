package com.pragma.powerup.domain.service;

import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import java.util.List;

@FunctionalInterface
public interface OrderHistoryProvider {
    List<OrderStatusAuditModel> getOrderHistory(Long orderId);
}

