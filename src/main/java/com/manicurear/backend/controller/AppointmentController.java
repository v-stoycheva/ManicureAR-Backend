package com.manicurear.backend.controller;

import com.manicurear.backend.model.Appointment;
import com.manicurear.backend.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Създаване на нова резервация
     * POST http://localhost:8080/api/appointments
     */
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment created = appointmentService.createAppointment(appointment);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Връщаме съобщението за грешка (напр. "Часът е зает"), за да го покажеш в Android приложението
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /**
     * История на резервациите за конкретен клиент
     * GET http://localhost:8080/api/appointments/client/{clientId}
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Appointment>> getClientHistory(@PathVariable Long clientId) {
        List<Appointment> history = appointmentService.getClientHistory(clientId);
        return ResponseEntity.ok(history);
    }

    /**
     * Всички резервации на конкретен маникюрист (за неговия календар)
     * GET http://localhost:8080/api/appointments/manicurist/{manicuristId}
     */
    @GetMapping("/manicurist/{manicuristId}")
    public ResponseEntity<List<Appointment>> getManicuristSchedule(@PathVariable Long manicuristId) {
        List<Appointment> schedule = appointmentService.getAppointmentsByManicurist(manicuristId);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Отмяна на резервация
     * PUT http://localhost:8080/api/appointments/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long id) {
        try {
            appointmentService.cancelAppointment(id);
            return ResponseEntity.ok("Резервацията е отменена успешно.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}