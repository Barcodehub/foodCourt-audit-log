package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;


public interface IOrderMetricsPersistencePort {

    Page<OrderStatusAuditModel> findByRestaurantAndDateRange(
            Long restaurantId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    List<OrderStatusAuditModel> findByOrderId(Long orderId);
}

