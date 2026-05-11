package com.manicurear.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "manicurist_id", nullable = false)
    private User manicurist;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Service service;

    @ManyToOne
    @JoinColumn(name = "ar_design_id")
    private ArDesign arDesign;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private String status = "BOOKED"; // 'BOOKED', 'COMPLETED', 'CANCELLED'

    private String googleEventId;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
