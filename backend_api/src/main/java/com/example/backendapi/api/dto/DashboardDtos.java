package com.example.backendapi.api.dto;

import com.example.backendapi.domain.enums.DefectSeverity;
import com.example.backendapi.domain.enums.DefectStatus;

import java.util.Map;

public class DashboardDtos {

    public record DashboardResponse(
            long totalDefects,
            long openDefects,
            long overdueActions,
            Map<DefectSeverity, Long> defectsBySeverity,
            Map<DefectStatus, Long> defectsByStatus
    ) {}
}
