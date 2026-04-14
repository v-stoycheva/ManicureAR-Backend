package com.manicurear.backend.service;

import com.manicurear.backend.model.User;
import com.manicurear.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        // Инициализираме енкодера за пароли
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Регистрация на нов потребител с хеширане на паролата.
     */
    @Transactional
    public User registerUser(User user) {
        // 1. Проверка дали имейлът вече е зает
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Потребител с този имейл вече съществува!");
        }

        // 2. ХЕШИРАНЕ НА ПАРОЛАТА (Критично за сигурността)
        String encodedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(encodedPassword);

        // 3. Запис в базата
        return userRepository.save(user);
    }

    /**
     * Търсене на потребител по имейл (използва се при Login)
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Проверка на паролата при вход (Login логика)
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Потребителят не е намерен"));
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
}