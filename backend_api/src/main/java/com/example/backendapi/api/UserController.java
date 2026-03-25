package com.example.backendapi.api;

import com.example.backendapi.api.dto.UserDtos;
import com.example.backendapi.domain.User;
import com.example.backendapi.service.UserService;
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
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management (ADMIN only)")
public class UserController {

    private final UserService users;

    public UserController(UserService users) {
        this.users = users;
    }

    @GetMapping
    @Operation(summary = "List users", description = "Returns paginated users. Optionally filter by active=true/false.")
    public Page<UserDtos.UserResponse> list(
            @RequestParam(required = false) Boolean active,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return users.list(active, pageable).map(UserDtos.UserResponse::from);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user", description = "Fetch a user by id.")
    public UserDtos.UserResponse get(@PathVariable Long id) {
        return UserDtos.UserResponse.from(users.getOrThrow(id));
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Create a new user.")
    public UserDtos.UserResponse create(@Valid @RequestBody UserDtos.CreateUserRequest req) {
        User u = new User()
                .setName(req.name())
                .setEmail(req.email())
                .setRole(req.role())
                .setActive(req.active() == null || req.active());
        return UserDtos.UserResponse.from(users.create(u));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user.")
    public UserDtos.UserResponse update(@PathVariable Long id, @Valid @RequestBody UserDtos.UpdateUserRequest req) {
        User patch = new User()
                .setName(req.name())
                .setEmail(req.email())
                .setRole(req.role())
                .setActive(req.active());
        return UserDtos.UserResponse.from(users.update(id, patch));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user.")
    public void delete(@PathVariable Long id) {
        users.delete(id);
    }
}
