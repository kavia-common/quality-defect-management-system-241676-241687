package com.example.backendapi.repo;

import com.example.backendapi.domain.CorrectiveAction;
import com.example.backendapi.domain.enums.CorrectiveActionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CorrectiveActionRepository extends JpaRepository<CorrectiveAction, Long> {

    Page<CorrectiveAction> findByStatus(CorrectiveActionStatus status, Pageable pageable);

    Page<CorrectiveAction> findByDueDateBefore(LocalDate date, Pageable pageable);

    Page<CorrectiveAction> findByDefectId(Long defectId, Pageable pageable);
}
