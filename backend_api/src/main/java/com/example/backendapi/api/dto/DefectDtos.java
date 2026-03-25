package com.example.backendapi.api.dto;

import com.example.backendapi.domain.Defect;
import com.example.backendapi.domain.enums.DefectSeverity;
import com.example.backendapi.domain.enums.DefectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

public class DefectDtos {

    public record DefectResponse(
            Long id,
            String title,
            String description,
            DefectSeverity severity,
            DefectStatus status,
            LocalDate reportedDate,
            String location,
            String productLine,
            IdName reportedBy,
            IdName assignedTo,
            Instant createdAt,
            Instant updatedAt
    ) {
        public static DefectResponse from(Defect d) {
            return new DefectResponse(
                    d.getId(),
                    d.getTitle(),
                    d.getDescription(),
                    d.getSeverity(),
                    d.getStatus(),
                    d.getReportedDate(),
                    d.getLocation(),
                    d.getProductLine(),
                    d.getReportedBy() == null ? null : new IdName(d.getReportedBy().getId(), d.getReportedBy().getName()),
                    d.getAssignedTo() == null ? null : new IdName(d.getAssignedTo().getId(), d.getAssignedTo().getName()),
                    d.getCreatedAt(),
                    d.getUpdatedAt()
            );
        }
    }

    public record IdName(Long id, String name) {}

    public record CreateDefectRequest(
            @NotBlank String title,
            @NotBlank String description,
            @NotNull DefectSeverity severity,
            DefectStatus status,
            LocalDate reportedDate,
            String location,
            String productLine,
            Long reportedById,
            Long assignedToId
    ) {}

    public record UpdateDefectRequest(
            @NotBlank String title,
            @NotBlank String description,
            @NotNull DefectSeverity severity,
            @NotNull DefectStatus status,
            @NotNull LocalDate reportedDate,
            String location,
            String productLine,
            Long reportedById,
            Long assignedToId
    ) {}
}
