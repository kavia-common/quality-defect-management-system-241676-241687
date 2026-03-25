package com.example.backendapi.api.dto;

import com.example.backendapi.domain.User;
import com.example.backendapi.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class UserDtos {

    public record UserResponse(
            Long id,
            String name,
            String email,
            UserRole role,
            boolean active,
            Instant createdAt,
            Instant updatedAt
    ) {
        public static UserResponse from(User u) {
            return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.isActive(), u.getCreatedAt(), u.getUpdatedAt());
        }
    }

    public record CreateUserRequest(
            @NotBlank String name,
            @Email @NotBlank String email,
            @NotNull UserRole role,
            Boolean active
    ) {}

    public record UpdateUserRequest(
            @NotBlank String name,
            @Email @NotBlank String email,
            @NotNull UserRole role,
            @NotNull Boolean active
    ) {}
}
