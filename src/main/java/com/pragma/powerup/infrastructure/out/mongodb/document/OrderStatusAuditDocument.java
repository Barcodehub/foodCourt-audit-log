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

@Document(collection = "order_status_audit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusAuditDocument {

    @Id
    private String id;

    @Field("order_id")
    @Indexed
    private Long orderId;

    @Field("restaurant_id")
    private Long restaurantId;

    @Field("client_id")
    @Indexed
    private Long clientId;

    @Field("previous_status")
    private String previousStatus;

    @Field("new_status")
    private String newStatus;

    @Field("changed_by_user_id")
    private Long changedByUserId;

    @Field("changed_by_role")
    private String changedByRole;

    @Field("changed_at")
    @Indexed
    @CreatedDate
    private LocalDateTime changedAt;

    @Field("action_type")
    @Indexed
    private String actionType;

    @Field("employee_id")
    private Long employeeId;

    @Field("ip_address")
    private String ipAddress;

    @Field("user_agent")
    private String userAgent;

    @Field("notes")
    private String notes;

    @Field("time_in_previous_status_minutes")
    private Long timeInPreviousStatusMinutes;
}

