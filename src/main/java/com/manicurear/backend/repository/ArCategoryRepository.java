package com.manicurear.backend.repository;

import com.manicurear.backend.model.ArCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArCategoryRepository extends JpaRepository<ArCategory, Long> { }
