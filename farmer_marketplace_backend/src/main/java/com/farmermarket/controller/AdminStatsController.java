package com.farmermarket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.farmermarket.dto.response.AdminStatsDTO;
import com.farmermarket.service.AdminStatsService;

@RestController
@RequestMapping("/api/admin/stats")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    private final AdminStatsService service;

    public AdminStatsController(AdminStatsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<AdminStatsDTO> stats() {
        return ResponseEntity.ok(service.getStats());
    }
}