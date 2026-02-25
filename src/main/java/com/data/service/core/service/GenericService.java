package com.data.service.core.service;

import com.data.service.core.mapper.EntityMapper;
import com.data.service.core.search.GenericSpecification;
import com.data.service.core.search.MetricRequest;
import com.data.service.core.search.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Collections;

/**
 * Abstract generic service providing standard CRUD and metric operations.
 *
 * @param <M> the model/DTO type
 * @param <E> the JPA entity type
 */
public class GenericService<M, E> {

    private final JpaRepository<E, Long> repository;
    private final JpaSpecificationExecutor<E> specExecutor;
    private final EntityMapper<M, E> mapper;
    private final Class<M> modelClass;

    public GenericService(JpaRepository<E, Long> repository,
            JpaSpecificationExecutor<E> specExecutor,
            EntityMapper<M, E> mapper,
            Class<M> modelClass) {
        this.repository = repository;
        this.specExecutor = specExecutor;
        this.mapper = mapper;
        this.modelClass = modelClass;
    }

    public Class<M> getModelClass() {
        return modelClass;
    }

    public List<M> findAll() {
        return repository.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    public M save(M model) {
        E entity = mapper.toEntity(model);
        E saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    public M findById(Long id) {
        return repository.findById(id)
                .map(mapper::toModel)
                .orElse(null);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @SuppressWarnings("unchecked")
    public Object getMetric(MetricRequest request) {
        Specification<E> spec = null;
        if (request.getFilters() != null) {
            for (SearchCriteria criteria : request.getFilters()) {
                Specification<E> nextSpec = new GenericSpecification<>(criteria);
                spec = (spec == null) ? nextSpec : spec.and(nextSpec);
            }
        }

        // Handle COUNT without group by efficiently
        if ((request.getGroupBy() == null || request.getGroupBy().isEmpty())
                && "COUNT".equalsIgnoreCase(request.getMetricType())) {
            return specExecutor.count(spec);
        }

        List<E> entities = specExecutor.findAll(spec);

        if (request.getGroupBy() != null && !request.getGroupBy().isEmpty()) {
            java.util.function.Function<E, Map<String, Object>> groupKeyExtractor = e -> {
                Map<String, Object> key = new LinkedHashMap<>();
                for (String g : request.getGroupBy()) {
                    key.put(g, getFieldValue(e, g));
                }
                return key;
            };

            return entities.stream()
                    .collect(Collectors.groupingBy(groupKeyExtractor))
                    .entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> result = new LinkedHashMap<>(entry.getKey());
                        result.put(request.getMetricType().toLowerCase(), aggregate(entry.getValue(), request));
                        return result;
                    }).collect(Collectors.toList());
        }

        return aggregate(entities, request);
    }

    private Object getFieldValue(Object obj, String fieldName) {
        if (obj == null || fieldName == null)
            return null;
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Object aggregate(List<E> list, MetricRequest request) {
        String type = request.getMetricType();
        String fieldName = request.getField();

        if ("COUNT".equalsIgnoreCase(type)) {
            return list.size();
        }

        if (list.isEmpty())
            return 0.0;

        if ("MAX".equalsIgnoreCase(type) || "MIN".equalsIgnoreCase(type)) {
            List<Comparable> values = list.stream()
                    .map(e -> {
                        Object v = getFieldValue(e, fieldName);
                        return v instanceof Comparable ? (Comparable) v : null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (values.isEmpty())
                return null;
            return "MAX".equalsIgnoreCase(type) ? Collections.max(values) : Collections.min(values);
        }

        List<Double> numValues = list.stream()
                .map(e -> {
                    Object v = getFieldValue(e, fieldName);
                    return v instanceof Number ? ((Number) v).doubleValue() : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (numValues.isEmpty())
            return 0.0;

        if ("SUM".equalsIgnoreCase(type)) {
            return numValues.stream().mapToDouble(Double::doubleValue).sum();
        } else if ("AVG".equalsIgnoreCase(type)) {
            return numValues.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }

        return 0.0;
    }
}
