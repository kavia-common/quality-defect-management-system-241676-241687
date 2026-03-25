package com.example.backendapi.service;

import com.example.backendapi.api.dto.DashboardDtos;
import com.example.backendapi.domain.CorrectiveAction;
import com.example.backendapi.domain.Defect;
import com.example.backendapi.domain.enums.DefectSeverity;
import com.example.backendapi.domain.enums.DefectStatus;
import com.example.backendapi.repo.CorrectiveActionRepository;
import com.example.backendapi.repo.DefectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final DefectRepository defects;
    private final CorrectiveActionRepository actions;

    public DashboardService(DefectRepository defects, CorrectiveActionRepository actions) {
        this.defects = defects;
        this.actions = actions;
    }

    public DashboardDtos.DashboardResponse getDashboard() {
        List<Defect> allDefects = defects.findAll();
        List<CorrectiveAction> allActions = actions.findAll();

        Map<DefectSeverity, Long> bySeverity = new EnumMap<>(DefectSeverity.class);
        for (DefectSeverity s : DefectSeverity.values()) {
            bySeverity.put(s, 0L);
        }

        Map<DefectStatus, Long> byStatus = new EnumMap<>(DefectStatus.class);
        for (DefectStatus s : DefectStatus.values()) {
            byStatus.put(s, 0L);
        }

        long open = 0;
        for (Defect d : allDefects) {
            bySeverity.put(d.getSeverity(), bySeverity.getOrDefault(d.getSeverity(), 0L) + 1);
            byStatus.put(d.getStatus(), byStatus.getOrDefault(d.getStatus(), 0L) + 1);
            if (d.getStatus() == DefectStatus.OPEN || d.getStatus() == DefectStatus.IN_PROGRESS) {
                open++;
            }
        }

        LocalDate today = LocalDate.now();
        long overdue = allActions.stream()
                .filter(a -> a.getDueDate() != null && a.getDueDate().isBefore(today))
                .filter(a -> a.getStatus() != null && a.getStatus().name().equals("OPEN") || a.getStatus().name().equals("IN_PROGRESS"))
                .count();

        return new DashboardDtos.DashboardResponse(
                allDefects.size(),
                open,
                overdue,
                bySeverity,
                byStatus
        );
    }
}
