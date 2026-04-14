package com.manicurear.backend.service;

import com.manicurear.backend.model.Appointment;
import com.manicurear.backend.repository.AppointmentRepository;
import com.manicurear.backend.repository.StaffAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final StaffAvailabilityRepository availabilityRepository;

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              StaffAvailabilityRepository availabilityRepository) {
        this.appointmentRepository = appointmentRepository;
        this.availabilityRepository = availabilityRepository;
    }

    /**
    *  Основен метод за създаване на резервация с бизнес валидации.
     *  */
    @Transactional
    public Appointment createAppointment(Appointment appointment) {
// 1. Проверка дали маникюристът изобщо е на работа в този интервал
        if (!isManicuristAvailable(appointment)) {
            throw new RuntimeException("Маникюристът не е на работа в избрания интервал.");
        }

        // 2. Проверка за припокриващи се вече съществуващи резервации
        if (isOverlapping(appointment)) {
            throw new RuntimeException("Избраният час вече е зает от друг клиент.");
        }

        // 3. Изчисляване на крайна цена (ако не е подадена)
        if (appointment.getTotalPrice() == null) {
            appointment.setTotalPrice(appointment.getService().getPrice());
        }

        return appointmentRepository.save(appointment);
    }

    /**
     * Логика за проверка на застъпване (Overlap Detection)
     */
    private boolean isOverlapping(Appointment newApp) {
        List<Appointment> existingApps = appointmentRepository.findByManicurist_UserIdAndStartTimeBetween(
                newApp.getManicurist().getUserId(),
                newApp.getStartTime().minusHours(4), // Гледаме прозорец от време
                newApp.getEndTime().plusHours(4)
        );

        return existingApps.stream().anyMatch(existing ->
                newApp.getStartTime().isBefore(existing.getEndTime()) &&
                        existing.getStartTime().isBefore(newApp.getEndTime())
        );
    }

    /**
     * Проверка дали часът влиза в работния график на служителя
     */
    private boolean isManicuristAvailable(Appointment app) {
        return availabilityRepository.findByManicurist_UserId(app.getManicurist().getUserId())
                .stream()
                .anyMatch(avail ->
                        !app.getStartTime().isBefore(avail.getStartTime()) &&
                                !app.getEndTime().isAfter(avail.getEndTime())
                );
    }

    // Методи за извличане на данни
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getClientHistory(Long clientId) {
        return appointmentRepository.findByClient_UserId(clientId);
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment app = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Резервацията не е намерена"));
        app.setStatus("CANCELLED");
        appointmentRepository.save(app);
    }

    // Намиране на всички резервации за конкретен маникюрист
    public List<Appointment> getAppointmentsByManicurist(Long manicuristId) {
        return appointmentRepository.findByManicurist_UserIdAndStartTimeBetween(
                manicuristId,
                OffsetDateTime.now().minusMonths(1),
                OffsetDateTime.now().plusMonths(1)
        );
    }
}
