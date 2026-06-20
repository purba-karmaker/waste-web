package com.wasteweb.controller;

import com.wasteweb.model.WasteAnalysis;
import com.wasteweb.model.WasteItem;
import com.wasteweb.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class WasteWebController {

    @Autowired
    private GeminiService geminiService;

    // Serve the main page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // API: analyze waste item
    @PostMapping("/api/analyze")
    @ResponseBody
    public ResponseEntity<?> analyzeWaste(@RequestBody WasteItem item) {
        try {
            if (item.getDescription() == null || item.getDescription().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Description is required"));
            }
            WasteAnalysis analysis = geminiService.analyzeWaste(item);
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "Analysis failed: " + e.getMessage()));
        }
    }

    // Health check
    @GetMapping("/api/health")
    @ResponseBody
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "WasteWeb is alive 🌱"));
    }
}
