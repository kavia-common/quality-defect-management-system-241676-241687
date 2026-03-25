package com.example.backendapi.service;

import com.example.backendapi.domain.User;
import com.example.backendapi.repo.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    public User getOrThrow(Long id) {
        return users.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + id));
    }

    public Page<User> list(Boolean active, Pageable pageable) {
        if (active == null) {
            return users.findAll(pageable);
        }
        return users.findByActive(active, pageable);
    }

    public User create(User u) {
        users.findByEmail(u.getEmail()).ifPresent(existing -> {
            throw new IllegalArgumentException("Email already exists: " + u.getEmail());
        });
        return users.save(u);
    }

    public User update(Long id, User patch) {
        User u = getOrThrow(id);
        // If email changes, enforce uniqueness
        if (!u.getEmail().equalsIgnoreCase(patch.getEmail())) {
            users.findByEmail(patch.getEmail()).ifPresent(existing -> {
                throw new IllegalArgumentException("Email already exists: " + patch.getEmail());
            });
        }
        u.setName(patch.getName())
                .setEmail(patch.getEmail())
                .setRole(patch.getRole())
                .setActive(patch.isActive());
        return users.save(u);
    }

    public void delete(Long id) {
        User u = getOrThrow(id);
        users.delete(u);
    }
}
