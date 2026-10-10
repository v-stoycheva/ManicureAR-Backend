package com.manicurear.backend.service;

import com.manicurear.backend.model.ArDesign;
import com.manicurear.backend.repository.ArDesignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArDesignService {

    private final ArDesignRepository arDesignRepository;

    @Autowired
    public ArDesignService(ArDesignRepository arDesignRepository) {
        this.arDesignRepository = arDesignRepository;
    }

    public List<ArDesign> getAllActiveDesigns() {
        return arDesignRepository.findAll().stream()
                .filter(ArDesign::getIsActive)
                .collect(Collectors.toList());
    }

    public List<ArDesign> getDesignsByCategory(Long categoryId) {
        return arDesignRepository.findAll().stream()
                .filter(d -> d.getCategory().getArCategoryId().equals(categoryId) && d.getIsActive())
                .collect(Collectors.toList());
    }

    public ArDesign getDesignById(Long id) {
        return arDesignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("The AR design was not found"));
    }

    public ArDesign saveDesign(ArDesign design) {
        return arDesignRepository.save(design);
    }
}