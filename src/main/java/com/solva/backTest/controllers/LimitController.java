package com.solva.backTest.controllers;

import com.solva.backTest.entities.Limit;
import com.solva.backTest.services.LimitService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/limits")
public class LimitController {

    private final LimitService limitService;

    public LimitController(LimitService limitService) {
        this.limitService = limitService;
    }

    @PostMapping
    public Limit setNewLimit(@RequestBody double limit) {
        return limitService.setNewLimit(limit);
    }

    @GetMapping
    public List<Limit> getAllLimits() {
        return limitService.getAllLimits();
    }
}

