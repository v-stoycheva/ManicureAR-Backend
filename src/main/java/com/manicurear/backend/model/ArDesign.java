package com.manicurear.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ar_designs")
@Getter @Setter @NoArgsConstructor
public class ArDesign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long arDesignId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ArCategory category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String filePath;

    @Column(columnDefinition = "jsonb")
    private String metadata; // Тук ще пазим специфичните за ARCore данни

    private Boolean isActive = true;
}