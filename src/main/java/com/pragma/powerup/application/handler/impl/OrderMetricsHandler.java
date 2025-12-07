package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.apifirst.model.*;
import com.pragma.powerup.application.handler.IOrderMetricsHandler;
import com.pragma.powerup.application.mapper.IOrderMetricsMapper;
import com.pragma.powerup.domain.api.IOrderMetricsServicePort;
import com.pragma.powerup.domain.model.EmployeeEfficiencyMetricsModel;
import com.pragma.powerup.domain.model.OrderDurationMetricsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderMetricsHandler implements IOrderMetricsHandler {

    private final IOrderMetricsServicePort metricsServicePort;
    private final IOrderMetricsMapper metricsMapper;

    @Override
    public OrdersDurationMetricsResponseDto getOrdersDurationMetrics(
            Long restaurantId,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    ) {
        LocalDateTime startDateTime = startDate != null ? startDate.toLocalDateTime() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.toLocalDateTime() : null;

        Pageable pageable = PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 20
        );

        OrderDurationMetricsModel metricsModel = metricsServicePort.getOrdersDurationMetrics(
                restaurantId, startDateTime, endDateTime, pageable
        );

        OrdersDurationMetricsResponseDto response = new OrdersDurationMetricsResponseDto();
        response.setData(metricsMapper.toOrdersDurationMetricsDataDto(metricsModel));

        PaginationMetaDto meta = new PaginationMetaDto();
        meta.setPage(page != null ? page : 0);
        meta.setSize(size != null ? size : 20);
        meta.setTotalElements(metricsModel.getOrders().size());
        meta.setTotalPages(1);
        response.setMeta(meta);

        return response;
    }

    @Override
    public EmployeeEfficiencyMetricsResponseDto getEmployeeEfficiencyMetrics(
            Long restaurantId,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            Integer minOrdersCompleted,
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    ) {
        LocalDateTime startDateTime = startDate != null ? startDate.toLocalDateTime() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.toLocalDateTime() : null;

        Pageable pageable = PageRequest.of(
                page != null ? page : 0,
                size != null ? size : 20
        );

        EmployeeEfficiencyMetricsModel metricsModel = metricsServicePort.getEmployeeEfficiencyMetrics(
                restaurantId, startDateTime, endDateTime, minOrdersCompleted, pageable
        );

        EmployeeEfficiencyMetricsResponseDto response = new EmployeeEfficiencyMetricsResponseDto();
        response.setData(metricsMapper.toEmployeeEfficiencyMetricsDataDto(metricsModel));

        PaginationMetaDto meta = new PaginationMetaDto();
        meta.setPage(page != null ? page : 0);
        meta.setSize(size != null ? size : 20);
        meta.setTotalElements(metricsModel.getRanking().size());
        meta.setTotalPages(1);
        response.setMeta(meta);

        return response;
    }
}


