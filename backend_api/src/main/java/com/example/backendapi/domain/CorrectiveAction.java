package com.example.backendapi.domain;

import com.example.backendapi.domain.enums.CorrectiveActionStatus;
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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "corrective_actions", indexes = {
        @Index(name = "idx_actions_status", columnList = "status"),
        @Index(name = "idx_actions_dueDate", columnList = "dueDate"),
        @Index(name = "idx_actions_defect", columnList = "defect_id"),
        @Index(name = "idx_actions_assignee", columnList = "assignee_id")
})
public class CorrectiveAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defect_id", nullable = false)
    private Defect defect;

    @NotBlank
    @Column(nullable = false, length = 2000)
    private String action;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CorrectiveActionStatus status = CorrectiveActionStatus.OPEN;

    @NotNull
    @Column(nullable = false)
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @Column(length = 4000)
    private String notes;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Defect getDefect() {
        return defect;
    }

    public CorrectiveAction setDefect(Defect defect) {
        this.defect = defect;
        return this;
    }

    public String getAction() {
        return action;
    }

    public CorrectiveAction setAction(String action) {
        this.action = action;
        return this;
    }

    public CorrectiveActionStatus getStatus() {
        return status;
    }

    public CorrectiveAction setStatus(CorrectiveActionStatus status) {
        this.status = status;
        return this;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public CorrectiveAction setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public User getAssignee() {
        return assignee;
    }

    public CorrectiveAction setAssignee(User assignee) {
        this.assignee = assignee;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public CorrectiveAction setNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
