package com.example.backendapi.repo;

import com.example.backendapi.domain.Rca;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RcaRepository extends JpaRepository<Rca, Long> {

    Optional<Rca> findByDefectId(Long defectId);
}
