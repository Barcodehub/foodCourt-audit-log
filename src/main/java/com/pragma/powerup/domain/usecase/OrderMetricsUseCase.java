package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderMetricsServicePort;
import com.pragma.powerup.domain.model.DurationSummaryModel;
import com.pragma.powerup.domain.model.EmployeeEfficiencyMetricModel;
import com.pragma.powerup.domain.model.EmployeeEfficiencyMetricsModel;
import com.pragma.powerup.domain.model.EmployeeEfficiencySummaryModel;
import com.pragma.powerup.domain.model.OrderDurationMetricModel;
import com.pragma.powerup.domain.model.OrderDurationMetricsModel;
import com.pragma.powerup.domain.model.OrderStatusAuditModel;
import com.pragma.powerup.domain.service.OrderMetricsCalculator;
import com.pragma.powerup.domain.spi.IOrderMetricsPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderMetricsUseCase implements IOrderMetricsServicePort {

    private final IOrderMetricsPersistencePort metricsPersistencePort;
    private final OrderMetricsCalculator calculator = new OrderMetricsCalculator();

    @Override
    public OrderDurationMetricsModel getOrdersDurationMetrics(
            Long restaurantId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    ) {
        Page<OrderStatusAuditModel> finalizedAuditsPage = metricsPersistencePort.findByRestaurantAndDateRange(
                restaurantId, startDate, endDate, pageable
        );

        List<OrderDurationMetricModel> metrics = calculator.calculateOrderDurations(
                finalizedAuditsPage.getContent(),
                metricsPersistencePort::findByOrderId
        );

        DurationSummaryModel summary = calculateDurationSummary(metrics);

        return OrderDurationMetricsModel.builder()
                .orders(metrics)
                .summary(summary)
                .build();
    }

    @Override
    public EmployeeEfficiencyMetricsModel getEmployeeEfficiencyMetrics(
            Long restaurantId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    ) {
        Page<OrderStatusAuditModel> finalizedAuditsPage = metricsPersistencePort.findByRestaurantAndDateRange(
                restaurantId, startDate, endDate, pageable
        );

        List<OrderDurationMetricModel> allMetrics = calculator.calculateOrderDurations(
                finalizedAuditsPage.getContent(),
                metricsPersistencePort::findByOrderId
        );

        List<OrderDurationMetricModel> metricsWithEmployee = allMetrics.stream()
                .filter(metric -> metric.getEmployeeId() != null)
               .toList();

        List<EmployeeEfficiencyMetricModel> employeeMetrics = calculateEmployeeMetrics(metricsWithEmployee);

        assignRanking(employeeMetrics);

        EmployeeEfficiencySummaryModel summary = calculateEfficiencySummary(employeeMetrics);

        return EmployeeEfficiencyMetricsModel.builder()
                .ranking(employeeMetrics)
                .summary(summary)
                .build();
    }

    private List<EmployeeEfficiencyMetricModel> calculateEmployeeMetrics(
            List<OrderDurationMetricModel> metrics
    ) {
        // Agrupar por empleado
        Map<Long, List<OrderDurationMetricModel>> metricsByEmployee = metrics.stream()
                .collect(Collectors.groupingBy(OrderDurationMetricModel::getEmployeeId));

        List<EmployeeEfficiencyMetricModel> employeeMetrics = new ArrayList<>();

        for (Map.Entry<Long, List<OrderDurationMetricModel>> entry : metricsByEmployee.entrySet()) {
            Long employeeId = entry.getKey();
            List<OrderDurationMetricModel> employeeOrders = entry.getValue();


            // Calcular estadísticas usando el calculator
            List<Long> durations = employeeOrders.stream()
                    .map(OrderDurationMetricModel::getDurationMinutes)
                    .sorted()
                   .toList();

            double average = durations.stream()
                    .mapToLong(Long::longValue)
                    .average()
                    .orElse(0.0);

            int deliveredCount = calculator.countDelivered(employeeOrders);
            int cancelledCount = calculator.countCancelled(employeeOrders);
            double median = calculator.calculateMedian(durations);

            EmployeeEfficiencyMetricModel metric = EmployeeEfficiencyMetricModel.builder()
                    .rank(0) // Se asigna después
                    .employeeId(employeeId)
                    .totalOrdersCompleted(employeeOrders.size())
                    .totalOrdersDelivered(deliveredCount)
                    .totalOrdersCancelled(cancelledCount)
                    .averageDurationMinutes(calculator.roundToTwoDecimals(average))
                    .minDurationMinutes(durations.isEmpty() ? 0L : durations.get(0))
                    .maxDurationMinutes(durations.isEmpty() ? 0L : durations.get(durations.size() - 1))
                    .medianDurationMinutes(calculator.roundToTwoDecimals(median))
                    .build();

            employeeMetrics.add(metric);
        }

        return employeeMetrics;
    }

    private DurationSummaryModel calculateDurationSummary(List<OrderDurationMetricModel> metrics) {
        if (metrics.isEmpty()) {
            return DurationSummaryModel.builder()
                    .totalOrders(0)
                    .averageDurationMinutes(0.0)
                    .minDurationMinutes(0L)
                    .maxDurationMinutes(0L)
                    .medianDurationMinutes(0.0)
                    .deliveredCount(0)
                    .cancelledCount(0)
                    .build();
        }

        List<Long> durations = metrics.stream()
                .map(OrderDurationMetricModel::getDurationMinutes)
                .sorted()
               .toList();

        double average = durations.stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);

        int deliveredCount = calculator.countDelivered(metrics);
        int cancelledCount = calculator.countCancelled(metrics);
        double median = calculator.calculateMedian(durations);

        return DurationSummaryModel.builder()
                .totalOrders(metrics.size())
                .averageDurationMinutes(calculator.roundToTwoDecimals(average))
                .minDurationMinutes(durations.get(0))
                .maxDurationMinutes(durations.get(durations.size() - 1))
                .medianDurationMinutes(calculator.roundToTwoDecimals(median))
                .deliveredCount(deliveredCount)
                .cancelledCount(cancelledCount)
                .build();
    }

    private EmployeeEfficiencySummaryModel calculateEfficiencySummary(List<EmployeeEfficiencyMetricModel> metrics) {
        if (metrics.isEmpty()) {
            return EmployeeEfficiencySummaryModel.builder()
                    .totalEmployees(0)
                    .restaurantAverageDurationMinutes(0.0)
                    .bestEmployeeAverageDurationMinutes(0.0)
                    .worstEmployeeAverageDurationMinutes(0.0)
                    .totalOrdersProcessed(0)
                    .build();
        }

        double restaurantAverage = metrics.stream()
                .mapToDouble(EmployeeEfficiencyMetricModel::getAverageDurationMinutes)
                .average()
                .orElse(0.0);

        double bestAverage = metrics.stream()
                .mapToDouble(EmployeeEfficiencyMetricModel::getAverageDurationMinutes)
                .min()
                .orElse(0.0);

        double worstAverage = metrics.stream()
                .mapToDouble(EmployeeEfficiencyMetricModel::getAverageDurationMinutes)
                .max()
                .orElse(0.0);

        int totalOrders = metrics.stream()
                .mapToInt(EmployeeEfficiencyMetricModel::getTotalOrdersCompleted)
                .sum();

        return EmployeeEfficiencySummaryModel.builder()
                .totalEmployees(metrics.size())
                .restaurantAverageDurationMinutes(calculator.roundToTwoDecimals(restaurantAverage))
                .bestEmployeeAverageDurationMinutes(calculator.roundToTwoDecimals(bestAverage))
                .worstEmployeeAverageDurationMinutes(calculator.roundToTwoDecimals(worstAverage))
                .totalOrdersProcessed(totalOrders)
                .build();
    }

    private void assignRanking(List<EmployeeEfficiencyMetricModel> metrics) {
        List<EmployeeEfficiencyMetricModel> sorted = metrics.stream()
                .sorted(Comparator.comparing(EmployeeEfficiencyMetricModel::getAverageDurationMinutes))
               .toList();

        for (int i = 0; i < sorted.size(); i++) {
            sorted.get(i).setRank(i + 1);
        }
    }
}

