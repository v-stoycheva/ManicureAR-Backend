package com.manicurear.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ar_categories")
@Getter @Setter @NoArgsConstructor
public class ArCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long arCategoryId;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;
}
