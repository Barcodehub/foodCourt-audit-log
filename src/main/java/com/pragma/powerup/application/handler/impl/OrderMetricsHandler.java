package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.apifirst.model.*;
import com.pragma.powerup.application.handler.IOrderMetricsHandler;
import com.pragma.powerup.application.mapper.IOrderMetricsMapper;
import com.pragma.powerup.domain.api.IOrderMetricsServicePort;
import com.pragma.powerup.domain.model.EmployeeEfficiencyMetricsModel;
import com.pragma.powerup.domain.model.OrderDurationMetricsModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        // Convertir OffsetDateTime a LocalDateTime
        LocalDateTime startDateTime = startDate != null ? startDate.toLocalDateTime() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.toLocalDateTime() : null;

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);

        Page<OrderDurationMetricsModel> metricsPage = metricsServicePort.getOrdersDurationMetrics(
                restaurantId, startDateTime, endDateTime, pageable
        );

        OrdersDurationMetricsResponseDto response = new OrdersDurationMetricsResponseDto();

        if (!metricsPage.getContent().isEmpty()) {
            OrderDurationMetricsModel metricsModel = metricsPage.getContent().get(0);

            // Crear el objeto data
            OrdersDurationMetricsResponseDataDto dataDto = new OrdersDurationMetricsResponseDataDto();
            dataDto.setOrders(metricsMapper.toOrderDurationMetricDtoList(metricsModel.getOrders()));
            dataDto.setSummary(metricsMapper.toDurationSummaryDto(metricsModel.getSummary()));

            response.setData(dataDto);
        } else {
            // Si no hay datos, crear respuesta vacía
            OrdersDurationMetricsResponseDataDto dataDto = new OrdersDurationMetricsResponseDataDto();
            dataDto.setOrders(java.util.Collections.emptyList());
            dataDto.setSummary(createEmptyDurationSummary());
            response.setData(dataDto);
        }

        // Crear metadata de paginación
        PaginationMetaDto meta = new PaginationMetaDto();
        meta.setPage(metricsPage.getNumber());
        meta.setSize(metricsPage.getSize());
        meta.setTotalElements((int) metricsPage.getTotalElements());
        meta.setTotalPages(metricsPage.getTotalPages());
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
        // Convertir OffsetDateTime a LocalDateTime
        LocalDateTime startDateTime = startDate != null ? startDate.toLocalDateTime() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.toLocalDateTime() : null;

        Pageable pageable = createPageable(page, size, sortBy, sortDirection);

        Page<EmployeeEfficiencyMetricsModel> metricsPage = metricsServicePort.getEmployeeEfficiencyMetrics(
                restaurantId, startDateTime, endDateTime, minOrdersCompleted, pageable
        );

        // Transformar a DTOs
        EmployeeEfficiencyMetricsResponseDto response = new EmployeeEfficiencyMetricsResponseDto();

        if (!metricsPage.getContent().isEmpty()) {
            EmployeeEfficiencyMetricsModel metricsModel = metricsPage.getContent().get(0);

            // Crear el objeto data
            EmployeeEfficiencyMetricsResponseDataDto dataDto = new EmployeeEfficiencyMetricsResponseDataDto();
            dataDto.setRanking(metricsMapper.toEmployeeEfficiencyMetricDtoList(metricsModel.getRanking()));
            dataDto.setSummary(metricsMapper.toEmployeeEfficiencySummaryDto(metricsModel.getSummary()));

            response.setData(dataDto);
        } else {
            // Si no hay datos, vacio
            EmployeeEfficiencyMetricsResponseDataDto dataDto = new EmployeeEfficiencyMetricsResponseDataDto();
            dataDto.setRanking(java.util.Collections.emptyList());
            dataDto.setSummary(createEmptyEfficiencySummary());
            response.setData(dataDto);
        }

        PaginationMetaDto meta = new PaginationMetaDto();
        meta.setPage(metricsPage.getNumber());
        meta.setSize(metricsPage.getSize());
        meta.setTotalElements((int) metricsPage.getTotalElements());
        meta.setTotalPages(metricsPage.getTotalPages());
        response.setMeta(meta);

        return response;
    }

    /**
     * Crea el objeto Pageable con configuración de paginación y ordenamiento
     */
    private Pageable createPageable(Integer page, Integer size, String sortBy, String sortDirection) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 20;
        String sortField = sortBy != null ? sortBy : "durationMinutes";
        String direction = sortDirection != null ? sortDirection : "DESC";

        Sort.Direction sortDir = "ASC".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDir, sortField);

        return PageRequest.of(pageNumber, pageSize, sort);
    }

    /**
     * Crea un resumen vacío de duración
     */
    private DurationSummaryDto createEmptyDurationSummary() {
        DurationSummaryDto summary = new DurationSummaryDto();
        summary.setTotalOrders(0);
        summary.setAverageDurationMinutes(0.0);
        summary.setMinDurationMinutes(0L);
        summary.setMaxDurationMinutes(0L);
        summary.setMedianDurationMinutes(0.0);
        summary.setDeliveredCount(0);
        summary.setCancelledCount(0);
        return summary;
    }

    /**
     * Crea un resumen vacío de eficiencia de empleados
     */
    private EmployeeEfficiencySummaryDto createEmptyEfficiencySummary() {
        EmployeeEfficiencySummaryDto summary = new EmployeeEfficiencySummaryDto();
        summary.setTotalEmployees(0);
        summary.setRestaurantAverageDurationMinutes(0.0);
        summary.setBestEmployeeAverageDurationMinutes(0.0);
        summary.setWorstEmployeeAverageDurationMinutes(0.0);
        summary.setTotalOrdersProcessed(0);
        return summary;
    }
}


