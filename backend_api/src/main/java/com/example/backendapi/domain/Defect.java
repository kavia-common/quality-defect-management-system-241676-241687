package com.example.backendapi.domain;

import com.example.backendapi.domain.enums.DefectSeverity;
import com.example.backendapi.domain.enums.DefectStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "defects", indexes = {
        @Index(name = "idx_defects_severity", columnList = "severity"),
        @Index(name = "idx_defects_status", columnList = "status"),
        @Index(name = "idx_defects_reportedDate", columnList = "reportedDate"),
        @Index(name = "idx_defects_assignedTo", columnList = "assignedTo_id")
})
public class Defect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank
    @Column(nullable = false, length = 4000)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DefectSeverity severity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DefectStatus status = DefectStatus.OPEN;

    @NotNull
    @Column(nullable = false)
    private LocalDate reportedDate = LocalDate.now();

    @Column(length = 120)
    private String location;

    @Column(length = 120)
    private String productLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportedBy_id")
    private User reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignedTo_id")
    private User assignedTo;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @OneToOne(mappedBy = "defect", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Rca rca;

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Defect setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Defect setDescription(String description) {
        this.description = description;
        return this;
    }

    public DefectSeverity getSeverity() {
        return severity;
    }

    public Defect setSeverity(DefectSeverity severity) {
        this.severity = severity;
        return this;
    }

    public DefectStatus getStatus() {
        return status;
    }

    public Defect setStatus(DefectStatus status) {
        this.status = status;
        return this;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public Defect setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Defect setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getProductLine() {
        return productLine;
    }

    public Defect setProductLine(String productLine) {
        this.productLine = productLine;
        return this;
    }

    public User getReportedBy() {
        return reportedBy;
    }

    public Defect setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
        return this;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public Defect setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Rca getRca() {
        return rca;
    }

    public Defect setRca(Rca rca) {
        this.rca = rca;
        return this;
    }
}
