package com.example.backendapi.api;

import com.example.backendapi.api.dto.DefectDtos;
import com.example.backendapi.domain.Defect;
import com.example.backendapi.domain.enums.DefectSeverity;
import com.example.backendapi.domain.enums.DefectStatus;
import com.example.backendapi.service.DefectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/defects")
@Tag(name = "Defects", description = "Defect logging and tracking")
public class DefectController {

    private final DefectService defects;

    public DefectController(DefectService defects) {
        this.defects = defects;
    }

    @GetMapping
    @Operation(summary = "List defects", description = "Paginated list with optional filtering: severity, status, from/to dates, q text search.")
    public Page<DefectDtos.DefectResponse> list(
            @RequestParam(required = false) DefectSeverity severity,
            @RequestParam(required = false) DefectStatus status,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return defects.list(severity, status, from, to, q, pageable).map(DefectDtos.DefectResponse::from);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get defect", description = "Fetch defect by id.")
    public DefectDtos.DefectResponse get(@PathVariable Long id) {
        return DefectDtos.DefectResponse.from(defects.getOrThrow(id));
    }

    @PostMapping
    @Operation(summary = "Create defect", description = "Create a new defect.")
    public DefectDtos.DefectResponse create(@Valid @RequestBody DefectDtos.CreateDefectRequest req) {
        Defect d = new Defect()
                .setTitle(req.title())
                .setDescription(req.description())
                .setSeverity(req.severity())
                .setStatus(req.status() == null ? DefectStatus.OPEN : req.status())
                .setReportedDate(req.reportedDate() == null ? LocalDate.now() : req.reportedDate())
                .setLocation(req.location())
                .setProductLine(req.productLine());
        return DefectDtos.DefectResponse.from(defects.create(d, req.reportedById(), req.assignedToId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update defect", description = "Update a defect.")
    public DefectDtos.DefectResponse update(@PathVariable Long id, @Valid @RequestBody DefectDtos.UpdateDefectRequest req) {
        Defect patch = new Defect()
                .setTitle(req.title())
                .setDescription(req.description())
                .setSeverity(req.severity())
                .setStatus(req.status())
                .setReportedDate(req.reportedDate())
                .setLocation(req.location())
                .setProductLine(req.productLine());
        return DefectDtos.DefectResponse.from(defects.update(id, patch, req.reportedById(), req.assignedToId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete defect", description = "Delete a defect.")
    public void delete(@PathVariable Long id) {
        defects.delete(id);
    }
}
