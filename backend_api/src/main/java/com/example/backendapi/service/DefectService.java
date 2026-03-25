package com.example.backendapi.service;

import com.example.backendapi.domain.Defect;
import com.example.backendapi.domain.User;
import com.example.backendapi.domain.enums.DefectSeverity;
import com.example.backendapi.domain.enums.DefectStatus;
import com.example.backendapi.repo.DefectRepository;
import com.example.backendapi.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class DefectService {

    private final DefectRepository defects;
    private final UserRepository users;

    public DefectService(DefectRepository defects, UserRepository users) {
        this.defects = defects;
        this.users = users;
    }

    public Defect getOrThrow(Long id) {
        return defects.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Defect not found: " + id));
    }

    public Page<Defect> list(DefectSeverity severity, DefectStatus status, LocalDate from, LocalDate to, String q, Pageable pageable) {
        if (q != null && !q.isBlank()) {
            return defects.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q, pageable);
        }
        if (severity != null) {
            return defects.findBySeverity(severity, pageable);
        }
        if (status != null) {
            return defects.findByStatus(status, pageable);
        }
        if (from != null && to != null) {
            return defects.findByReportedDateBetween(from, to, pageable);
        }
        return defects.findAll(pageable);
    }

    public Defect create(Defect d, Long reportedById, Long assignedToId) {
        linkUsers(d, reportedById, assignedToId);
        return defects.save(d);
    }

    public Defect update(Long id, Defect patch, Long reportedById, Long assignedToId) {
        Defect d = getOrThrow(id);
        d.setTitle(patch.getTitle())
                .setDescription(patch.getDescription())
                .setSeverity(patch.getSeverity())
                .setStatus(patch.getStatus())
                .setReportedDate(patch.getReportedDate())
                .setLocation(patch.getLocation())
                .setProductLine(patch.getProductLine());
        linkUsers(d, reportedById, assignedToId);
        return defects.save(d);
    }

    public void delete(Long id) {
        defects.delete(getOrThrow(id));
    }

    private void linkUsers(Defect d, Long reportedById, Long assignedToId) {
        User reported = reportedById == null ? null : users.findById(reportedById).orElseThrow(() ->
                new IllegalArgumentException("reportedById not found: " + reportedById));
        User assigned = assignedToId == null ? null : users.findById(assignedToId).orElseThrow(() ->
                new IllegalArgumentException("assignedToId not found: " + assignedToId));
        d.setReportedBy(reported);
        d.setAssignedTo(assigned);
    }
}
