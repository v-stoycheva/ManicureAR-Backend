package com.manicurear.backend.controller;

import com.manicurear.backend.model.ArDesign;
import com.manicurear.backend.service.ArDesignService;
import com.manicurear.backend.repository.ArDesignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ar-designs")
@CrossOrigin(origins = "*")
public class ArDesignController {

    private final ArDesignService arDesignService;
    private final ArDesignRepository arDesignRepository;

    @Autowired
    public ArDesignController(ArDesignService arDesignService, ArDesignRepository arDesignRepository) {
        this.arDesignService = arDesignService;
        this.arDesignRepository = arDesignRepository;
    }

    @GetMapping
    public ResponseEntity<List<ArDesign>> getAllDesigns() {
        return ResponseEntity.ok(arDesignRepository.findAllByIsActiveTrue());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ArDesign>> getDesignsByCategory(@PathVariable Long categoryId) {
        List<ArDesign> designs = arDesignService.getDesignsByCategory(categoryId);
        return ResponseEntity.ok(designs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArDesign> getDesignById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(arDesignService.getDesignById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ArDesign> createDesign(@RequestBody ArDesign design) {
        return ResponseEntity.ok(arDesignService.saveDesign(design));
    }
}