package com.manicurear.backend.service;

import com.manicurear.backend.model.ArDesign;
import com.manicurear.backend.model.User;
import com.manicurear.backend.repository.ArDesignRepository;
import com.manicurear.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ArDesignRepository arDesignRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ArDesignRepository arDesignRepository) {
        this.userRepository = userRepository;
        this.arDesignRepository = arDesignRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("A user with this email address already exists!");
        }

        String encodedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);

        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User updateProfile(Long userId, User updatedDetails) {
        User user = getUserById(userId);

        // Обновяваме само ако в заявката има изпратена нова стойност
        if (updatedDetails.getFirstName() != null) user.setFirstName(updatedDetails.getFirstName());
        if (updatedDetails.getLastName() != null) user.setLastName(updatedDetails.getLastName());
        if (updatedDetails.getEmail() != null) user.setEmail(updatedDetails.getEmail());
        if (updatedDetails.getPhone() != null) user.setPhone(updatedDetails.getPhone());
        if (updatedDetails.getBio() != null) user.setBio(updatedDetails.getBio());

        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1. Проверка дали старата парола е вярна
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // 2. Хеширане и запис на новата парола
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void toggleFavoriteDesign(Long userId, Long designId) {
        // 1. Проверява дали потребителят съществува
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Проверява дали дизайнът съществува
        ArDesign design = arDesignRepository.findById(designId)
                .orElseThrow(() -> new RuntimeException("Design not found"));

        // 3. Проверка дали списъкът е NULL
        if (user.getFavoriteDesigns() == null) {
            user.setFavoriteDesigns(new HashSet<>());
        }

        // 4. Логика за добавяне/премахване
        if (user.getFavoriteDesigns().contains(design)) {
            user.getFavoriteDesigns().remove(design);
        } else {
            user.getFavoriteDesigns().add(design);
        }

        userRepository.save(user);
    }

    public List<User> getUsersByRole(String roleName) {
        return userRepository.findByRole_Name(roleName);
    }
}