package com.manicurear.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty; // Важен импорт!
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("user_id")
    private Long userId;

    @Column(unique = true, nullable = false)
    @JsonProperty("email")
    private String email;

    @Column(nullable = false)
    @JsonProperty("password_hash")
    private String passwordHash;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("profile_picture_url")
    private String profilePictureUrl;

    @JsonProperty("bio")
    private String bio;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonProperty("role")
    private Role role;

    @JsonProperty("is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    @JsonProperty("created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "ar_design_id")
    )
    @JsonProperty("favorite_designs")
    private Set<ArDesign> favoriteDesigns = new HashSet<>();
}