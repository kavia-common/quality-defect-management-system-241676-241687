package com.example.backendapi.api;

import com.example.backendapi.api.dto.CorrectiveActionDtos;
import com.example.backendapi.domain.CorrectiveAction;
import com.example.backendapi.domain.enums.CorrectiveActionStatus;
import com.example.backendapi.service.CorrectiveActionService;
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

@RestController
@RequestMapping("/api/corrective-actions")
@Tag(name = "Corrective Actions", description = "Track corrective actions with due dates and assignees")
public class CorrectiveActionController {

    private final CorrectiveActionService actions;

    public CorrectiveActionController(CorrectiveActionService actions) {
        this.actions = actions;
    }

    @GetMapping
    @Operation(summary = "List corrective actions", description = "Paginated list. Filters: defectId, status, overdueOnly.")
    public Page<CorrectiveActionDtos.CorrectiveActionResponse> list(
            @RequestParam(required = false) Long defectId,
            @RequestParam(required = false) CorrectiveActionStatus status,
            @RequestParam(required = false) Boolean overdueOnly,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return actions.list(defectId, status, overdueOnly, pageable).map(CorrectiveActionDtos.CorrectiveActionResponse::from);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get corrective action", description = "Fetch corrective action by id.")
    public CorrectiveActionDtos.CorrectiveActionResponse get(@PathVariable Long id) {
        return CorrectiveActionDtos.CorrectiveActionResponse.from(actions.getOrThrow(id));
    }

    @PostMapping
    @Operation(summary = "Create corrective action", description = "Create corrective action for a defect.")
    public CorrectiveActionDtos.CorrectiveActionResponse create(@Valid @RequestBody CorrectiveActionDtos.CreateCorrectiveActionRequest req) {
        CorrectiveAction a = new CorrectiveAction()
                .setAction(req.action())
                .setStatus(req.status())
                .setDueDate(req.dueDate())
                .setNotes(req.notes());
        return CorrectiveActionDtos.CorrectiveActionResponse.from(actions.create(a, req.defectId(), req.assigneeId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update corrective action", description = "Update corrective action.")
    public CorrectiveActionDtos.CorrectiveActionResponse update(@PathVariable Long id, @Valid @RequestBody CorrectiveActionDtos.UpdateCorrectiveActionRequest req) {
        CorrectiveAction patch = new CorrectiveAction()
                .setAction(req.action())
                .setStatus(req.status())
                .setDueDate(req.dueDate())
                .setNotes(req.notes());
        return CorrectiveActionDtos.CorrectiveActionResponse.from(actions.update(id, patch, req.defectId(), req.assigneeId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete corrective action", description = "Delete corrective action.")
    public void delete(@PathVariable Long id) {
        actions.delete(id);
    }
}
