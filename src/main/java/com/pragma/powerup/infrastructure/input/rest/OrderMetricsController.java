package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.apifirst.api.MetricsApi;
import com.pragma.powerup.apifirst.model.EmployeeEfficiencyMetricsResponseDto;
import com.pragma.powerup.apifirst.model.OrdersDurationMetricsResponseDto;
import com.pragma.powerup.application.handler.IOrderMetricsHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
public class OrderMetricsController implements MetricsApi {

    private final IOrderMetricsHandler orderMetricsHandler;

    @Override
    public ResponseEntity<EmployeeEfficiencyMetricsResponseDto> getEmployeeEfficiencyMetrics(Long restaurantId, OffsetDateTime startDate, OffsetDateTime endDate, Integer minOrdersCompleted, Integer page, Integer size, String sortBy, String sortDirection) {
        return null;
    }

    @Override
    public ResponseEntity<OrdersDurationMetricsResponseDto> getOrdersDurationMetrics(Long restaurantId, OffsetDateTime startDate, OffsetDateTime endDate, Integer page, Integer size, String sortBy, String sortDirection) {
        return null;
    }
}
