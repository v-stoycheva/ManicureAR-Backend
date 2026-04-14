package com.manicurear.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "staff_availability")
@Getter @Setter @NoArgsConstructor
public class StaffAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long staffAvailabilityId;

    @ManyToOne
    @JoinColumn(name = "manicurist_id")
    private User manicurist;

    @Column(nullable = false)
    private OffsetDateTime startTime;

    @Column(nullable = false)
    private OffsetDateTime endTime;
}
