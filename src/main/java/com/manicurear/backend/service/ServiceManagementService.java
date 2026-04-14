package com.manicurear.backend.service;

import com.manicurear.backend.model.Service;
import com.manicurear.backend.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@org.springframework.stereotype.Service // Използваме пълното име, за да не се бърка с твоя модел Service
public class ServiceManagementService {

    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceManagementService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Service getServiceById(Long id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Услугата не е намерена"));
    }

    public Service saveService(Service service) {
        return serviceRepository.save(service);
    }
}