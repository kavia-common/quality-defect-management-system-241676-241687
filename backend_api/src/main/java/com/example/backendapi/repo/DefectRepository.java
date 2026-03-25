package com.example.backendapi.repo;

import com.example.backendapi.domain.Defect;
import com.example.backendapi.domain.enums.DefectSeverity;
import com.example.backendapi.domain.enums.DefectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DefectRepository extends JpaRepository<Defect, Long> {

    Page<Defect> findBySeverity(DefectSeverity severity, Pageable pageable);

    Page<Defect> findByStatus(DefectStatus status, Pageable pageable);

    Page<Defect> findByReportedDateBetween(LocalDate from, LocalDate to, Pageable pageable);

    Page<Defect> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);
}
