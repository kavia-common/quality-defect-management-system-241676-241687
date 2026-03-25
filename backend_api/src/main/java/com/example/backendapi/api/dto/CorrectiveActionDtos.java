package com.example.backendapi.api.dto;

import com.example.backendapi.domain.CorrectiveAction;
import com.example.backendapi.domain.enums.CorrectiveActionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

public class CorrectiveActionDtos {

    public record CorrectiveActionResponse(
            Long id,
            Long defectId,
            String defectTitle,
            String action,
            CorrectiveActionStatus status,
            LocalDate dueDate,
            IdName assignee,
            String notes,
            Instant createdAt,
            Instant updatedAt
    ) {
        public static CorrectiveActionResponse from(CorrectiveAction a) {
            return new CorrectiveActionResponse(
                    a.getId(),
                    a.getDefect().getId(),
                    a.getDefect().getTitle(),
                    a.getAction(),
                    a.getStatus(),
                    a.getDueDate(),
                    a.getAssignee() == null ? null : new IdName(a.getAssignee().getId(), a.getAssignee().getName()),
                    a.getNotes(),
                    a.getCreatedAt(),
                    a.getUpdatedAt()
            );
        }
    }

    public record IdName(Long id, String name) {}

    public record CreateCorrectiveActionRequest(
            @NotNull Long defectId,
            @NotBlank String action,
            @NotNull CorrectiveActionStatus status,
            @NotNull LocalDate dueDate,
            Long assigneeId,
            String notes
    ) {}

    public record UpdateCorrectiveActionRequest(
            @NotNull Long defectId,
            @NotBlank String action,
            @NotNull CorrectiveActionStatus status,
            @NotNull LocalDate dueDate,
            Long assigneeId,
            String notes
    ) {}
}
