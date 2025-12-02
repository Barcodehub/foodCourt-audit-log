package com.pragma.powerup.infrastructure.out.mongodb.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

/**
 * Documento MongoDB para auditoría de cambios de estado de pedidos
 * Sigue las mejores prácticas de MongoDB:
 * - Índices optimizados para consultas frecuentes
 * - Nombres de campos consistentes
 * - Estructura desnormalizada para mejor rendimiento
 */
@Document(collection = "order_status_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusAuditDocument {

    @Id
    private String id;

    // Identificadores - Indexados para búsquedas rápidas
    @Field("order_id")
    @Indexed
    private Long orderId;

    @Field("restaurant_id")
    private Long restaurantId;

    @Field("client_id")
    @Indexed
    private Long clientId;

    // Estado
    @Field("previous_status")
    private String previousStatus;

    @Field("new_status")
    private String newStatus;

    // Quién hizo el cambio
    @Field("changed_by_user_id")
    private Long changedByUserId;

    @Field("changed_by_role")
    private String changedByRole; // CLIENTE, EMPLEADO, SYSTEM

    // Cuándo - Indexado para ordenamiento temporal
    @Field("changed_at")
    @Indexed
    @CreatedDate
    private LocalDateTime changedAt;

    // Contexto adicional
    @Field("action_type")
    @Indexed
    private String actionType;

    @Field("employee_id")
    private Long employeeId; // Si fue asignado a un empleado

    // Metadata (opcional pero útil)
    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    @Field("notes")
    private String notes;

    // Tiempo de procesamiento
    @Field("time_in_previous_status_minutes")
    private Long timeInPreviousStatusMinutes;
}

