package com.example.backendapi.api;

import com.example.backendapi.api.dto.DashboardDtos;
import com.example.backendapi.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Aggregated metrics for dashboards")
public class DashboardController {

    private final DashboardService dashboard;

    public DashboardController(DashboardService dashboard) {
        this.dashboard = dashboard;
    }

    @GetMapping
    @Operation(summary = "Get dashboard metrics", description = "Returns defect counts, distributions, and overdue action alerts.")
    public DashboardDtos.DashboardResponse get() {
        return dashboard.getDashboard();
    }
}
