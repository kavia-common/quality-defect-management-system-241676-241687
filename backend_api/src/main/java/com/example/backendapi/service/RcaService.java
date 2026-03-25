package com.example.backendapi.service;

import com.example.backendapi.domain.Defect;
import com.example.backendapi.domain.Rca;
import com.example.backendapi.repo.RcaRepository;
import org.springframework.stereotype.Service;

@Service
public class RcaService {

    private final RcaRepository rcas;
    private final DefectService defects;

    public RcaService(RcaRepository rcas, DefectService defects) {
        this.rcas = rcas;
        this.defects = defects;
    }

    public Rca getByDefectIdOrNull(Long defectId) {
        return rcas.findByDefectId(defectId).orElse(null);
    }

    public Rca upsert(Long defectId, Rca patch) {
        Defect d = defects.getOrThrow(defectId);
        Rca r = rcas.findByDefectId(defectId).orElseGet(() -> new Rca().setDefect(d));
        r.setWhy1(patch.getWhy1())
                .setWhy2(patch.getWhy2())
                .setWhy3(patch.getWhy3())
                .setWhy4(patch.getWhy4())
                .setWhy5(patch.getWhy5())
                .setRootCause(patch.getRootCause());
        return rcas.save(r);
    }

    public void deleteByDefectId(Long defectId) {
        rcas.findByDefectId(defectId).ifPresent(rcas::delete);
    }
}
