package com.data.service.core.controller;

import com.data.service.core.model.CryptoAsset;
import com.data.service.core.service.CryptoAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.data.service.core.search.MetricRequest;

@RestController
@RequestMapping("/api/cryptoassets")
@RequiredArgsConstructor
public class CryptoAssetController {

    private final CryptoAssetService service;

    @GetMapping
    public List<CryptoAsset> getAll() {
        return service.findAll();
    }

    @PostMapping
    public CryptoAsset create(@RequestBody CryptoAsset entity) {
        return service.save(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CryptoAsset> getById(@PathVariable Long id) {
        CryptoAsset entity = service.findById(id);
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
