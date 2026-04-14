package com.manicurear.backend.controller;

import com.manicurear.backend.model.Service;
import com.manicurear.backend.service.ServiceManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceController {

    private final ServiceManagementService serviceManagementService;

    @Autowired
    public ServiceController(ServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }

    /**
     * Връща всички предлагани услуги (Ценоразпис)
     * GET http://localhost:8080/api/services
     */
    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(serviceManagementService.getAllServices());
    }

    /**
     * Връща детайли за конкретна услуга
     * GET http://localhost:8080/api/services/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(serviceManagementService.getServiceById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Администраторски метод за добавяне на нова услуга
     * POST http://localhost:8080/api/services
     */
    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        return ResponseEntity.ok(serviceManagementService.saveService(service));
    }
}