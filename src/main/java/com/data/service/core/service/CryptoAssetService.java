package com.data.service.core.service;

import com.data.service.core.model.CryptoAsset;
import com.data.service.core.model.CryptoAssetEntity;
import com.data.service.core.repository.CryptoAssetRepository;
import com.data.service.core.mapper.CryptoAssetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CryptoAssetService {

    private final CryptoAssetRepository repository;
    private final CryptoAssetMapper mapper;

    public List<CryptoAsset> findAll() {
        return repository.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    public CryptoAsset save(CryptoAsset model) {
        CryptoAssetEntity entity = mapper.toEntity(model);
        CryptoAssetEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    public CryptoAsset findById(Long id) {
        return repository.findById(id)
                .map(mapper::toModel)
                .orElse(null);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
