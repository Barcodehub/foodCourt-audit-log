package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDurationMetricModel {

    private Long orderId;
    private Long clientId;
    private Long employeeId;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String finalStatus;
    private Long durationMinutes;
}

