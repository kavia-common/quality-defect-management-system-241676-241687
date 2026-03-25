package com.example.backendapi.service;

import com.example.backendapi.domain.CorrectiveAction;
import com.example.backendapi.domain.Defect;
import com.example.backendapi.domain.User;
import com.example.backendapi.domain.enums.CorrectiveActionStatus;
import com.example.backendapi.repo.CorrectiveActionRepository;
import com.example.backendapi.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class CorrectiveActionService {

    private final CorrectiveActionRepository actions;
    private final DefectService defects;
    private final UserRepository users;

    public CorrectiveActionService(CorrectiveActionRepository actions, DefectService defects, UserRepository users) {
        this.actions = actions;
        this.defects = defects;
        this.users = users;
    }

    public CorrectiveAction getOrThrow(Long id) {
        return actions.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Corrective action not found: " + id));
    }

    public Page<CorrectiveAction> list(Long defectId, CorrectiveActionStatus status, Boolean overdueOnly, Pageable pageable) {
        if (defectId != null) {
            return actions.findByDefectId(defectId, pageable);
        }
        if (Boolean.TRUE.equals(overdueOnly)) {
            return actions.findByDueDateBefore(LocalDate.now(), pageable);
        }
        if (status != null) {
            return actions.findByStatus(status, pageable);
        }
        return actions.findAll(pageable);
    }

    public CorrectiveAction create(CorrectiveAction a, Long defectId, Long assigneeId) {
        Defect d = defects.getOrThrow(defectId);
        a.setDefect(d);
        a.setAssignee(resolveAssignee(assigneeId));
        return actions.save(a);
    }

    public CorrectiveAction update(Long id, CorrectiveAction patch, Long defectId, Long assigneeId) {
        CorrectiveAction a = getOrThrow(id);
        Defect d = defects.getOrThrow(defectId);
        a.setDefect(d)
                .setAction(patch.getAction())
                .setStatus(patch.getStatus())
                .setDueDate(patch.getDueDate())
                .setNotes(patch.getNotes())
                .setAssignee(resolveAssignee(assigneeId));
        return actions.save(a);
    }

    public void delete(Long id) {
        actions.delete(getOrThrow(id));
    }

    private User resolveAssignee(Long assigneeId) {
        if (assigneeId == null) {
            return null;
        }
        return users.findById(assigneeId).orElseThrow(() ->
                new IllegalArgumentException("assigneeId not found: " + assigneeId));
    }
}
