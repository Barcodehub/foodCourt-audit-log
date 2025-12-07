package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.EmployeeEfficiencyMetricsModel;
import com.pragma.powerup.domain.model.OrderDurationMetricsModel;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IOrderMetricsServicePort {

    OrderDurationMetricsModel getOrdersDurationMetrics(
            Long restaurantId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );

    EmployeeEfficiencyMetricsModel getEmployeeEfficiencyMetrics(
            Long restaurantId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    );
}

