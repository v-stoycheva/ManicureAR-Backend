package com.manicurear.backend.controller;

import com.manicurear.backend.model.ArDesign;
import com.manicurear.backend.service.ArDesignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ar-designs")
@CrossOrigin(origins = "*")
public class ArDesignController {

    private final ArDesignService arDesignService;

    @Autowired
    public ArDesignController(ArDesignService arDesignService) {
        this.arDesignService = arDesignService;
    }

    /**
     * Връща списък с всички активни AR дизайни
     * GET http://localhost:8080/api/ar-designs
     */
    @GetMapping
    public ResponseEntity<List<ArDesign>> getAllDesigns() {
        return ResponseEntity.ok(arDesignService.getAllActiveDesigns());
    }

    /**
     * Връща дизайни от конкретна категория (напр. само цветове или само форми)
     * GET http://localhost:8080/api/ar-designs/category/{categoryId}
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ArDesign>> getDesignsByCategory(@PathVariable Long categoryId) {
        List<ArDesign> designs = arDesignService.getDesignsByCategory(categoryId);
        return ResponseEntity.ok(designs);
    }

    /**
     * Детайли за конкретен дизайн (включително метаданни за ARCore)
     * GET http://localhost:8080/api/ar-designs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ArDesign> getDesignById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(arDesignService.getDesignById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Администраторски метод: добавяне на нов AR дизайн
     * POST http://localhost:8080/api/ar-designs
     */
    @PostMapping
    public ResponseEntity<ArDesign> createDesign(@RequestBody ArDesign design) {
        return ResponseEntity.ok(arDesignService.saveDesign(design));
    }
}