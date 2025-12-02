package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Modelo de dominio para auditoría de cambios de estado
 * Representa la lógica de negocio sin dependencias de infraestructura
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusAuditModel {

    private String id; // String para compatibilidad con MongoDB ObjectId
    private Long orderId;
    private Long restaurantId;
    private Long clientId;
    private String previousStatus;
    private String newStatus;
    private Long changedByUserId;
    private String changedByRole;
    private LocalDateTime changedAt;
    private String actionType;
    private Long employeeId;
    private String ipAddress;
    private String userAgent;
    private String notes;
    private Long timeInPreviousStatusMinutes;
}

