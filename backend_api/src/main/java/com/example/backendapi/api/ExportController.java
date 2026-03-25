package com.example.backendapi.api;

import com.example.backendapi.repo.CorrectiveActionRepository;
import com.example.backendapi.repo.DefectRepository;
import com.example.backendapi.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
@Tag(name = "Export", description = "Export reports for audit readiness")
public class ExportController {

    private final DefectRepository defects;
    private final CorrectiveActionRepository actions;
    private final ExportService exportService;

    public ExportController(DefectRepository defects, CorrectiveActionRepository actions, ExportService exportService) {
        this.defects = defects;
        this.actions = actions;
        this.exportService = exportService;
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Export PDF report", description = "Exports defects + corrective actions summary as a PDF.")
    public ResponseEntity<byte[]> exportPdf() {
        byte[] pdf = exportService.exportDefectsPdf(defects.findAll(), actions.findAll());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"quality-defect-report.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
