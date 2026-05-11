package com.manicurear.backend.service;

import com.manicurear.backend.model.StaffAvailability;
import com.manicurear.backend.repository.StaffAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StaffAvailabilityService {

    private final StaffAvailabilityRepository availabilityRepository;

    @Autowired
    public StaffAvailabilityService(StaffAvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    public List<StaffAvailability> getAvailabilityByManicurist(Long manicuristId) {
        return availabilityRepository.findByManicurist_UserId(manicuristId);
    }
}