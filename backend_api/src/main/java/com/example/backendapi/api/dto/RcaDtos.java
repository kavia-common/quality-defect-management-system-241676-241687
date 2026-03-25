package com.example.backendapi.api.dto;

import com.example.backendapi.domain.Rca;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class RcaDtos {

    public record RcaResponse(
            Long id,
            Long defectId,
            String why1,
            String why2,
            String why3,
            String why4,
            String why5,
            String rootCause,
            Instant createdAt,
            Instant updatedAt
    ) {
        public static RcaResponse from(Rca r) {
            return new RcaResponse(
                    r.getId(),
                    r.getDefect().getId(),
                    r.getWhy1(),
                    r.getWhy2(),
                    r.getWhy3(),
                    r.getWhy4(),
                    r.getWhy5(),
                    r.getRootCause(),
                    r.getCreatedAt(),
                    r.getUpdatedAt()
            );
        }
    }

    public record UpsertRcaRequest(
            @NotNull Long defectId,
            @Size(max = 2000) String why1,
            @Size(max = 2000) String why2,
            @Size(max = 2000) String why3,
            @Size(max = 2000) String why4,
            @Size(max = 2000) String why5,
            @Size(max = 2000) String rootCause
    ) {}
}
