package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DurationSummaryModel {

    private Integer totalOrders;
    private Double averageDurationMinutes;
    private Long minDurationMinutes;
    private Long maxDurationMinutes;
    private Double medianDurationMinutes;
    private Integer deliveredCount;
    private Integer cancelledCount;
}

