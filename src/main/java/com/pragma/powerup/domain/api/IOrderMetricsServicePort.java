package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.EmployeeEfficiencyMetricsModel;
import com.pragma.powerup.domain.model.OrderDurationMetricsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IOrderMetricsServicePort {


    Page<OrderDurationMetricsModel> getOrdersDurationMetrics(
            Long restaurantId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    Page<EmployeeEfficiencyMetricsModel> getEmployeeEfficiencyMetrics(
            Long restaurantId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer minOrdersCompleted,
            Pageable pageable
    );
}

