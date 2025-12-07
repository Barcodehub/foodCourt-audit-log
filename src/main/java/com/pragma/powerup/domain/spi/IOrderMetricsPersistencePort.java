package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderStatusAuditModel;

import java.time.LocalDateTime;
import java.util.List;


public interface IOrderMetricsPersistencePort {

    List<OrderStatusAuditModel> findByRestaurantAndDateRange(
            Long restaurantId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<OrderStatusAuditModel> findByOrderId(Long orderId);
}

