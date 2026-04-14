package com.manicurear.backend.repository;

import com.manicurear.backend.model.StaffAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StaffAvailabilityRepository extends JpaRepository<StaffAvailability, Long> {
    List<StaffAvailability> findByManicurist_UserId(Long manicuristId);
}