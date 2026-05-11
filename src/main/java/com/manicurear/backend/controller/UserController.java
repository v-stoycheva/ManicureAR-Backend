package com.manicurear.backend.controller;

import com.manicurear.backend.model.ArDesign;
import com.manicurear.backend.model.User;
import com.manicurear.backend.repository.UserRepository;
import com.manicurear.backend.service.ServiceManagementService;
import com.manicurear.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.manicurear.backend.repository.ArDesignRepository;
import com.manicurear.backend.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Позволява на мобилното приложение да се свързва без проблеми
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ArDesignRepository arDesignRepository;

    @Autowired
    public UserController(UserService userService,
                          UserRepository userRepository,
                          ArDesignRepository arDesignRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.arDesignRepository = arDesignRepository;
    }

    /**
     * Регистрация на нов потребител
     * POST http://localhost:8080/api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Вход в системата (Опростена версия за момента)
     * POST http://localhost:8080/api/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestParam String email, @RequestParam String password) {
        return userService.findByEmail(email)
                .filter(user -> userService.checkPassword(password, user.getPasswordHash()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(401).build()); // 401 Unauthorized
    }

    /**
     * Извличане на данни за конкретен потребител
     * GET http://localhost:8080/api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserProfile(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Списък с всички потребители (за администраторския панел)
     * GET http://localhost:8080/api/users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Обновяване на профила
     * PUT http://localhost:8080/api/users/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<User> updateProfile(@PathVariable Long id, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateProfile(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id,
                                            @RequestParam String currentPassword,
                                            @RequestParam String newPassword) {
        try {
            userService.changePassword(id, currentPassword, newPassword);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{userId}/favorites/{designId}")
    public ResponseEntity<?> toggleFavorite(@PathVariable Long userId, @PathVariable Long designId) {
        // 1. Провери дали потребителят съществува
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Провери дали дизайнът съществува
        ArDesign design = arDesignRepository.findById(designId)
                .orElseThrow(() -> new RuntimeException("Design not found"));

        // 3. Критично: Провери дали списъкът е NULL (ако е, инициирай го)
        if (user.getFavoriteDesigns() == null) {
            user.setFavoriteDesigns(new HashSet<>());
        }

        // 4. Логика за добавяне/премахване
        if (user.getFavoriteDesigns().contains(design)) {
            user.getFavoriteDesigns().remove(design);
        } else {
            user.getFavoriteDesigns().add(design);
        }

        userRepository.save(user); // Записваме промените в базата
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/favorites")
    public ResponseEntity<Set<ArDesign>> getFavorites(@PathVariable Long userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(user.getFavoriteDesigns()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/manicurists")
    public ResponseEntity<List<User>> getManicurists() {
        List<User> artists = userService.getUsersByRole("ROLE_MANICURIST");
        return ResponseEntity.ok(artists);
    }

}