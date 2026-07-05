package com.manicurear.backend.service;

import com.manicurear.backend.model.User;
import com.manicurear.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;
    private User mockUser;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
        passwordEncoder = new BCryptPasswordEncoder();

        mockUser = new User();
        mockUser.setEmail("viktoria.test@gmail.com");
        mockUser.setPasswordHash("RawPassword123!");
        mockUser.setFirstName("Виктория");
        mockUser.setLastName("Стойчева");
    }

    @Test
    public void testRegisterUser_Success_ShouldHashPassword() {
        // Симулираме, че имейлът е свободен и не съществува в базата данни
        Mockito.when(userRepository.existsByEmail("viktoria.test@gmail.com"))
                .thenReturn(false);

        Mockito.when(userRepository.save(any(User.class)))
                .thenReturn(mockUser);

        User registered = userService.registerUser(mockUser);

        assertNotNull(registered);
        // Верифицираме, че паролата е преминала през кодиране и не е чист текст
        assertNotEquals("RawPassword123!", registered.getPasswordHash());
        assertTrue(passwordEncoder.matches("RawPassword123!", registered.getPasswordHash()));

        Mockito.verify(userRepository, Mockito.times(1)).save(mockUser);
    }

    @Test
    public void testRegisterUser_ThrowsException_WhenEmailExists() {
        // Симулираме, че имейлът ВЕЧЕ съществува в PostgreSQL
        Mockito.when(userRepository.existsByEmail("viktoria.test@gmail.com"))
                .thenReturn(true);

        // Проверяваме дали системата хвърля грешка и спира записа
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(mockUser);
        });

        assertEquals("Потребител с този имейл вече съществува!", exception.getMessage());
        Mockito.verify(userRepository, Mockito.never()).save(any());
    }
}