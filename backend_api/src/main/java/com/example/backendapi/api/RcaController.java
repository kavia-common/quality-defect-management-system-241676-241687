package com.example.backendapi.api;

import com.example.backendapi.api.dto.RcaDtos;
import com.example.backendapi.domain.Rca;
import com.example.backendapi.service.RcaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/rca")
@Tag(name = "RCA", description = "Root Cause Analysis (5 Whys) per defect")
public class RcaController {

    private final RcaService rca;

    public RcaController(RcaService rca) {
        this.rca = rca;
    }

    @GetMapping("/defect/{defectId}")
    @Operation(summary = "Get RCA by defect", description = "Returns RCA for a given defect, if it exists.")
    public RcaDtos.RcaResponse getByDefect(@PathVariable Long defectId) {
        Rca found = rca.getByDefectIdOrNull(defectId);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RCA not found for defect: " + defectId);
        }
        return RcaDtos.RcaResponse.from(found);
    }

    @PutMapping("/defect/{defectId}")
    @Operation(summary = "Upsert RCA by defect", description = "Creates or updates RCA for the defect.")
    public RcaDtos.RcaResponse upsert(@PathVariable Long defectId, @Valid @RequestBody RcaDtos.UpsertRcaRequest req) {
        if (!defectId.equals(req.defectId())) {
            throw new IllegalArgumentException("Path defectId must match body defectId");
        }
        Rca patch = new Rca()
                .setWhy1(req.why1())
                .setWhy2(req.why2())
                .setWhy3(req.why3())
                .setWhy4(req.why4())
                .setWhy5(req.why5())
                .setRootCause(req.rootCause());
        return RcaDtos.RcaResponse.from(rca.upsert(defectId, patch));
    }

    @DeleteMapping("/defect/{defectId}")
    @Operation(summary = "Delete RCA by defect", description = "Deletes the RCA record for a defect if present.")
    public void deleteByDefect(@PathVariable Long defectId) {
        rca.deleteByDefectId(defectId);
    }
}
