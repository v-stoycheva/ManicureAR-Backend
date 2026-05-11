package com.manicurear.backend.controller;

import com.manicurear.backend.model.StaffAvailability;
import com.manicurear.backend.service.StaffAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff-availability")
@CrossOrigin(origins = "*")
public class StaffAvailabilityController {

    private final StaffAvailabilityService availabilityService;

    @Autowired
    public StaffAvailabilityController(StaffAvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    /**
     * GET http://localhost:8080/api/staff-availability/manicurist/{manicuristId}
     */
    @GetMapping("/manicurist/{manicuristId}")
    public ResponseEntity<List<StaffAvailability>> getManicuristAvailability(@PathVariable Long manicuristId) {
        List<StaffAvailability> availability = availabilityService.getAvailabilityByManicurist(manicuristId);
        return ResponseEntity.ok(availability);
    }
}