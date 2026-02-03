package com.data.service.core.service;

import com.data.service.core.model.CryptoAsset;
import com.data.service.core.model.CryptoAssetEntity;
import com.data.service.core.repository.CryptoAssetRepository;
import com.data.service.core.mapper.CryptoAssetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.data.service.core.search.MetricRequest;
import com.data.service.core.search.SearchCriteria;
import com.data.service.core.search.GenericSpecification;
import org.springframework.data.jpa.domain.Specification;

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

    public Object getMetric(MetricRequest request) {
        Specification<CryptoAssetEntity> spec = null;
        if (request.getFilters() != null) {
            for (SearchCriteria criteria : request.getFilters()) {
                Specification<CryptoAssetEntity> nextSpec = new GenericSpecification<>(criteria);
                spec = (spec == null) ? nextSpec : spec.and(nextSpec);
            }
        }

        if ("COUNT".equalsIgnoreCase(request.getMetricType())) {
            return repository.count(spec);
        }

        List<CryptoAssetEntity> entities = repository.findAll(spec);

        if ("SUM".equalsIgnoreCase(request.getMetricType())) {
            return entities.stream().mapToDouble(e -> {
                try {
                    java.lang.reflect.Field field = e.getClass().getDeclaredField(request.getField());
                    field.setAccessible(true);
                    Object v = field.get(e);
                    return v instanceof Number ? ((Number) v).doubleValue() : 0.0;
                } catch (Exception ex) {
                    return 0.0;
                }
            }).sum();
        }

        return 0;
    }
}
