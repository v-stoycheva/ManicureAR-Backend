package com.manicurear.backend.service;

import com.manicurear.backend.model.Appointment;
import com.manicurear.backend.model.Service;
import com.manicurear.backend.model.User;
import com.manicurear.backend.repository.AppointmentRepository;
import com.manicurear.backend.repository.StaffAvailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class AppointmentServiceTest {

    private AppointmentRepository appointmentRepository;
    private StaffAvailabilityRepository availabilityRepository;
    private AppointmentService appointmentService;

    private Appointment mockAppointment;
    private User manicurist;
    private Service service;

    @BeforeEach
    public void setUp() {
        appointmentRepository = Mockito.mock(AppointmentRepository.class);
        availabilityRepository = Mockito.mock(StaffAvailabilityRepository.class);
        appointmentService = new AppointmentService(appointmentRepository, availabilityRepository);

        manicurist = new User();
        manicurist.setUserId(1L);
        manicurist.setFirstName("Георги");

        service = new Service();
        service.setServiceId(1L);
        service.setTitle("Класически Маникюр");
        service.setDurationMinutes(60); // 1 час времетраене
        service.setPrice(BigDecimal.valueOf(30.00));

        mockAppointment = new Appointment();
        mockAppointment.setManicurist(manicurist);
        mockAppointment.setService(service);
        mockAppointment.setStartTime(LocalDateTime.of(2026, 5, 20, 10, 0)); // 10:00 часа
        mockAppointment.setEndTime(LocalDateTime.of(2026, 5, 20, 11, 0));   // Конфигуриран краен час
    }

    @Test
    public void testCreateAppointment_Success() {
        // Симулираме, че маникюристът Е на работа от 09:00 до 17:00
        com.manicurear.backend.model.StaffAvailability availability = new com.manicurear.backend.model.StaffAvailability();
        availability.setStartTime(LocalDateTime.of(2026, 5, 20, 9, 0));
        availability.setEndTime(LocalDateTime.of(2026, 5, 20, 17, 0));

        Mockito.when(availabilityRepository.findByManicurist_UserId(1L))
                .thenReturn(List.of(availability));

        // Симулираме, че НЯМА застъпващи се часове за деня в базата данни
        Mockito.when(appointmentRepository.findByManicurist_UserIdAndStartTimeBetween(eq(1L), any(), any()))
                .thenReturn(Collections.emptyList());

        Mockito.when(appointmentRepository.save(any(Appointment.class)))
                .thenReturn(mockAppointment);

        Appointment created = appointmentService.createAppointment(mockAppointment);

        assertNotNull(created);
        assertEquals(LocalDateTime.of(2026, 5, 20, 11, 0), created.getEndTime()); // 10:00 + 60 мин = 11:00
        Mockito.verify(appointmentRepository, Mockito.times(1)).save(mockAppointment);
    }

    @Test
    public void testCreateAppointment_ThrowsException_WhenOverlapping() {
        // Симулираме, че маникюристът Е на работа в дефинирания ден
        com.manicurear.backend.model.StaffAvailability availability = new com.manicurear.backend.model.StaffAvailability();
        availability.setStartTime(LocalDateTime.of(2026, 5, 20, 9, 0));
        availability.setEndTime(LocalDateTime.of(2026, 5, 20, 17, 0));
        Mockito.when(availabilityRepository.findByManicurist_UserId(1L)).thenReturn(List.of(availability));

        // Симулираме СЪЩЕСТВУВАЩ зает час от 10:30 до 11:30, който пресича нашия интервал
        Appointment existingAppointment = new Appointment();
        existingAppointment.setStartTime(LocalDateTime.of(2026, 5, 20, 10, 30));
        existingAppointment.setEndTime(LocalDateTime.of(2026, 5, 20, 11, 30));

        Mockito.when(appointmentRepository.findByManicurist_UserIdAndStartTimeBetween(eq(1L), any(), any()))
                .thenReturn(List.of(existingAppointment));

        // Верифицираме, че алгоритъмът прихваща застъпването и хвърля изключение
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.createAppointment(mockAppointment);
        });

        assertEquals("Избраният час вече е зает от друг клиент.", exception.getMessage());
        Mockito.verify(appointmentRepository, Mockito.never()).save(any());
    }

    @Test
    public void testCancelAppointment_ThrowsException_WhenLessThan24Hours() {
        // Симулираме критичен сценарий: час, който започва след само 2 часа от текущия момент
        LocalDateTime appointmentStart = LocalDateTime.now().plusHours(2);

        Appointment urgentAppointment = new Appointment();
        urgentAppointment.setAppointmentId(55L);
        urgentAppointment.setStartTime(appointmentStart);
        urgentAppointment.setStatus("BOOKED");

        // Настройваме Mock репозиторито да върне този обект при търсене по ID
        Mockito.when(appointmentRepository.findById(55L))
                .thenReturn(Optional.of(urgentAppointment));

        // Верифицираме софтуерното налагане на времевото бизнес ограничение
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            appointmentService.cancelAppointment(55L);
        });

        assertEquals("Too late to cancel (less than 24 hours).", exception.getMessage());

        // Потвърждаваме, че състоянието на обекта в паметта НЕ е компрометирано
        assertNotEquals("CANCELLED", urgentAppointment.getStatus());
        Mockito.verify(appointmentRepository, Mockito.never()).save(any());
    }
}