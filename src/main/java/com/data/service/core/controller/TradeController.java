package com.data.service.core.controller;

import com.data.service.core.model.Trade;
import com.data.service.core.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.data.service.core.search.MetricRequest;

@RestController
@RequestMapping("/api/trades")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService service;

    @GetMapping
    public List<Trade> getAll() {
        return service.findAll();
    }

    @PostMapping
    public Trade create(@RequestBody Trade entity) {
        return service.save(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trade> getById(@PathVariable Long id) {
        Trade entity = service.findById(id);
        return entity != null ? ResponseEntity.ok(entity) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/metric")
    public ResponseEntity<Object> getMetric(@RequestBody MetricRequest request) {
        return ResponseEntity.ok(service.getMetric(request));
    }
}
