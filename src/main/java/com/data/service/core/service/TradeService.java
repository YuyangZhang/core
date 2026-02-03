package com.data.service.core.service;

import com.data.service.core.model.Trade;
import com.data.service.core.model.TradeEntity;
import com.data.service.core.repository.TradeRepository;
import com.data.service.core.mapper.TradeMapper;
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
public class TradeService {

    private final TradeRepository repository;
    private final TradeMapper mapper;

    public List<Trade> findAll() {
        return repository.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    public Trade save(Trade model) {
        TradeEntity entity = mapper.toEntity(model);
        TradeEntity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    public Trade findById(Long id) {
        return repository.findById(id)
                .map(mapper::toModel)
                .orElse(null);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Object getMetric(MetricRequest request) {
        Specification<TradeEntity> spec = null;
        if (request.getFilters() != null) {
            for (SearchCriteria criteria : request.getFilters()) {
                Specification<TradeEntity> nextSpec = new GenericSpecification<>(criteria);
                spec = (spec == null) ? nextSpec : spec.and(nextSpec);
            }
        }

        if ("COUNT".equalsIgnoreCase(request.getMetricType())) {
            return repository.count(spec);
        }

        List<TradeEntity> entities = repository.findAll(spec);

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
