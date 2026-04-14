package com.manicurear.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
@Getter @Setter @NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(unique = true, nullable = false)
    private String name; // 'ROLE_CLIENT', 'ROLE_MANICURIST', 'ROLE_ADMIN'
}