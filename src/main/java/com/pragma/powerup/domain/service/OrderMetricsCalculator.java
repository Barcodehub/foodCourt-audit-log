package com.pragma.powerup.domain.service;

import com.pragma.powerup.domain.model.OrderDurationMetricModel;
import com.pragma.powerup.domain.model.OrderStatusAuditModel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class OrderMetricsCalculator {

    public static final String STATUS_ENTREGADO = "ENTREGADO";
    public static final String STATUS_CANCELADO = "CANCELADO";
    public static final String STATUS_DESCONOCIDO = "DESCONOCIDO";

    private static final List<String> PENDING_STATUSES = Arrays.asList(
            "PENDIENTE", "Pendiente", "PENDIENT", "PENDING"
    );

    private static final List<String> DELIVERED_STATUSES = Arrays.asList(
            "Entregado", STATUS_ENTREGADO, "DELIVERED"
    );

    private static final List<String> CANCELLED_STATUSES = Arrays.asList(
            STATUS_CANCELADO, "Cancelado", "CANCELLED"
    );


    public List<OrderDurationMetricModel> calculateOrderDurations(
            List<OrderStatusAuditModel> finalizedOrderAudits,
            OrderHistoryProvider allOrderHistoriesProvider
    ) {
        Map<Long, OrderDurationMetricModel> metricsMap = new HashMap<>();

        for (OrderStatusAuditModel finalAudit : finalizedOrderAudits) {
            Long orderId = finalAudit.getOrderId();

            // Evitar procesar el mismo pedido múltiples veces
            if (metricsMap.containsKey(orderId)) {
                continue;
            }

            // Obtener historial completo del pedido
            List<OrderStatusAuditModel> orderHistory = allOrderHistoriesProvider.getOrderHistory(orderId);

            if (orderHistory.isEmpty()) {
                continue;
            }

            // Buscar el primer estado Pendiente
            Optional<OrderStatusAuditModel> startAudit = orderHistory.stream()
                    .filter(audit -> isPendingStatus(audit.getNewStatus()))
                    .findFirst();

            if (!startAudit.isPresent()) {
                continue;
            }

            LocalDateTime startedAt = startAudit.get().getChangedAt();
            LocalDateTime completedAt = finalAudit.getChangedAt();

            // Calcular duración en minutos
            long durationMinutes = Duration.between(startedAt, completedAt).toMinutes();

            // Normalizar estado final
            String normalizedStatus = normalizeFinalStatus(finalAudit.getNewStatus());

            OrderDurationMetricModel metric = OrderDurationMetricModel.builder()
                    .orderId(orderId)
                    .clientId(finalAudit.getClientId())
                    .employeeId(finalAudit.getEmployeeId())
                    .startedAt(startedAt)
                    .completedAt(completedAt)
                    .finalStatus(normalizedStatus)
                    .durationMinutes(durationMinutes)
                    .build();

            metricsMap.put(orderId, metric);
        }

        return new ArrayList<>(metricsMap.values());
    }

    public double calculateMedian(List<Long> sortedValues) {
        int size = sortedValues.size();
        if (size == 0) {
            return 0.0;
        }
        if (size % 2 == 0) {
            return (sortedValues.get(size / 2 - 1) + sortedValues.get(size / 2)) / 2.0;
        } else {
            return sortedValues.get(size / 2).doubleValue();
        }
    }

    public double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public boolean isPendingStatus(String status) {
        if (status == null) {
            return false;
        }
        return PENDING_STATUSES.stream()
                .anyMatch(s -> s.equalsIgnoreCase(status));
    }

    public boolean isDeliveredStatus(String status) {
        if (status == null) {
            return false;
        }
        return DELIVERED_STATUSES.stream()
                .anyMatch(s -> s.equalsIgnoreCase(status));
    }

    public boolean isCancelledStatus(String status) {
        if (status == null) {
            return false;
        }
        return CANCELLED_STATUSES.stream()
                .anyMatch(s -> s.equalsIgnoreCase(status));
    }

    public String normalizeFinalStatus(String status) {
        if (status == null) {
            return STATUS_DESCONOCIDO;
        }

        if (isDeliveredStatus(status)) {
            return STATUS_ENTREGADO;
        } else if (isCancelledStatus(status)) {
            return STATUS_CANCELADO;
        }

        return status;
    }

    //cuenta pedidos
    public int countDelivered(List<OrderDurationMetricModel> metrics) {
        return (int) metrics.stream()
                .filter(m -> STATUS_ENTREGADO.equalsIgnoreCase(m.getFinalStatus()))
                .count();
    }

    public int countCancelled(List<OrderDurationMetricModel> metrics) {
        return (int) metrics.stream()
                .filter(m -> STATUS_CANCELADO.equalsIgnoreCase(m.getFinalStatus()))
                .count();
    }
}

