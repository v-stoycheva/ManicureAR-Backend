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

    /**
     * Връща всички активни AR дизайни.
     */
    public List<ArDesign> getAllActiveDesigns() {
        return arDesignRepository.findAll().stream()
                .filter(ArDesign::getIsActive)
                .collect(Collectors.toList());
    }

    /**
     * Връща дизайни по конкретна категория (напр. 'Nail Shape' или 'Color').
     */
    public List<ArDesign> getDesignsByCategory(Long categoryId) {
        return arDesignRepository.findAll().stream()
                .filter(d -> d.getCategory().getArCategoryId().equals(categoryId) && d.getIsActive())
                .collect(Collectors.toList());
    }

    /**
     * Търсене на конкретен дизайн по ID (използва се при детайлен преглед в AR).
     */
    public ArDesign getDesignById(Long id) {
        return arDesignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AR дизайнът не е намерен"));
    }

    /**
     * Метод за администратора: добавяне на нов дизайн (път към файл, метаданни).
     */
    public ArDesign saveDesign(ArDesign design) {
        return arDesignRepository.save(design);
    }
}