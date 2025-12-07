package com.pragma.powerup.application.mapper;

import com.pragma.powerup.apifirst.model.*;
import com.pragma.powerup.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderMetricsMapper {

    // Mappers para OrderDurationMetric
    OrderDurationMetricDto toOrderDurationMetricDto(OrderDurationMetricModel model);
    List<OrderDurationMetricDto> toOrderDurationMetricDtoList(List<OrderDurationMetricModel> models);

    // Mappers para DurationSummary
    DurationSummaryDto toDurationSummaryDto(DurationSummaryModel model);

    // Mapper para OrderDurationMetrics completo
    OrdersDurationMetricsResponseDataDto toOrdersDurationMetricsDataDto(OrderDurationMetricsModel model);

    // Mappers para EmployeeEfficiencyMetric
    EmployeeEfficiencyMetricDto toEmployeeEfficiencyMetricDto(EmployeeEfficiencyMetricModel model);
    List<EmployeeEfficiencyMetricDto> toEmployeeEfficiencyMetricDtoList(List<EmployeeEfficiencyMetricModel> models);

    // Mappers para EmployeeEfficiencySummary
    EmployeeEfficiencySummaryDto toEmployeeEfficiencySummaryDto(EmployeeEfficiencySummaryModel model);

    // Mapper para EmployeeEfficiencyMetrics completo
    EmployeeEfficiencyMetricsResponseDataDto toEmployeeEfficiencyMetricsDataDto(EmployeeEfficiencyMetricsModel model);

    default OffsetDateTime map(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atOffset(ZoneOffset.UTC);
    }
}

