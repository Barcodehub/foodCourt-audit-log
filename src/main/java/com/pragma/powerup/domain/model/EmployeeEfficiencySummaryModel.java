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
public class EmployeeEfficiencySummaryModel {

    private Integer totalEmployees;
    private Double restaurantAverageDurationMinutes;
    private Double bestEmployeeAverageDurationMinutes;
    private Double worstEmployeeAverageDurationMinutes;
    private Integer totalOrdersProcessed;
}

