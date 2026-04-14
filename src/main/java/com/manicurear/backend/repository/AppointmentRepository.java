package com.manicurear.backend.repository;

import com.manicurear.backend.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Намира всички резервации на клиент
    List<Appointment> findByClient_UserId(Long clientId);

    // Намира резервациите на маникюрист в определен интервал
    List<Appointment> findByManicurist_UserIdAndStartTimeBetween(
            Long manicuristId, OffsetDateTime start, OffsetDateTime end
    );
}