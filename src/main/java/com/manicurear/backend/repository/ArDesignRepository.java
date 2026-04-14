package com.manicurear.backend.repository;


import com.manicurear.backend.model.ArDesign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArDesignRepository extends JpaRepository<ArDesign, Long> { }
