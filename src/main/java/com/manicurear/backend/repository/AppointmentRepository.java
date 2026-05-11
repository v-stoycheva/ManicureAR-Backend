package com.manicurear.backend.repository;

import com.manicurear.backend.model.Appointment;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Намира всички резервации на клиент
    List<Appointment> findByClient_UserId(Long clientId);

    List<Appointment> findByManicurist_UserId(Long manicuristId);

    // Намира резервациите на маникюрист в определен интервал
    List<Appointment> findByManicurist_UserIdAndStartTimeBetween(
            Long manicuristId, LocalDateTime start, LocalDateTime end
    );
}