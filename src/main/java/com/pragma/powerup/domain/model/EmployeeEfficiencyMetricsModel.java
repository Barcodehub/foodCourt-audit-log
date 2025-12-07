package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeEfficiencyMetricsModel {

    private List<EmployeeEfficiencyMetricModel> ranking;
    private EmployeeEfficiencySummaryModel summary;
}

