package com.example.backendapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;

@Entity
@Table(name = "rca", uniqueConstraints = {
        @UniqueConstraint(name = "uq_rca_defect", columnNames = {"defect_id"})
})
public class Rca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defect_id", nullable = false)
    private Defect defect;

    @Size(max = 2000)
    @Column(length = 2000)
    private String why1;

    @Size(max = 2000)
    @Column(length = 2000)
    private String why2;

    @Size(max = 2000)
    @Column(length = 2000)
    private String why3;

    @Size(max = 2000)
    @Column(length = 2000)
    private String why4;

    @Size(max = 2000)
    @Column(length = 2000)
    private String why5;

    @Size(max = 2000)
    @Column(length = 2000)
    private String rootCause;

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

    public Rca setDefect(Defect defect) {
        this.defect = defect;
        return this;
    }

    public String getWhy1() {
        return why1;
    }

    public Rca setWhy1(String why1) {
        this.why1 = why1;
        return this;
    }

    public String getWhy2() {
        return why2;
    }

    public Rca setWhy2(String why2) {
        this.why2 = why2;
        return this;
    }

    public String getWhy3() {
        return why3;
    }

    public Rca setWhy3(String why3) {
        this.why3 = why3;
        return this;
    }

    public String getWhy4() {
        return why4;
    }

    public Rca setWhy4(String why4) {
        this.why4 = why4;
        return this;
    }

    public String getWhy5() {
        return why5;
    }

    public Rca setWhy5(String why5) {
        this.why5 = why5;
        return this;
    }

    public String getRootCause() {
        return rootCause;
    }

    public Rca setRootCause(String rootCause) {
        this.rootCause = rootCause;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
