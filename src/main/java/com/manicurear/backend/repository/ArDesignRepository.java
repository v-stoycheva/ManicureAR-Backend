package com.manicurear.backend.repository;

import com.manicurear.backend.model.ArDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArDesignRepository extends JpaRepository<ArDesign, Long> {
    List<ArDesign> findAllByIsActiveTrue();
}