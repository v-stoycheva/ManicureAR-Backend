package com.manicurear.backend.service; // Тестов пакет в приложното ядро

import com.manicurear.backend.controller.ServiceController;
import com.manicurear.backend.model.Service;
import com.manicurear.backend.service.ServiceManagementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiceController.class)
public class ServiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceManagementService serviceManagementService;

    @Test
    public void testGetAllServices_ShouldReturnJsonArray_AndStatusOk() throws Exception {
        // 1. Подготовка на тестовите данни (Fixture Setup)
        Service mockService = new Service(
                1L,
                "Ноктопластика",
                "Дълготрайно изграждане с гел",
                90,
                BigDecimal.valueOf(50.00),
                "http://firebase.storage/nail.jpg"
        );

        List<Service> allServices = List.of(mockService);

        // 2. Стабване на поведението на сервизния слой през MockBean
        Mockito.when(serviceManagementService.getAllServices()).thenReturn(allServices);

        // 3. Изпълнение на симулирана HTTP GET заявка и верификация на резултата
        mockMvc.perform(get("/api/services")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].serviceId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Ноктопластика"))
                .andExpect(jsonPath("$[0].price").value(50.00))
                .andExpect(jsonPath("$[0].durationMinutes").value(90));

        // 4. Проверка дали сервизният метод е бил повикан точно веднъж
        Mockito.verify(serviceManagementService, Mockito.times(1)).getAllServices();
    }
}