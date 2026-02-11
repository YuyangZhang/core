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

    public Object getMetric(MetricRequest request) {
        Specification<E> spec = null;
        if (request.getFilters() != null) {
            for (SearchCriteria criteria : request.getFilters()) {
                Specification<E> nextSpec = new GenericSpecification<>(criteria);
                spec = (spec == null) ? nextSpec : spec.and(nextSpec);
            }
        }

        if ("COUNT".equalsIgnoreCase(request.getMetricType())) {
            return specExecutor.count(spec);
        }

        List<E> entities = specExecutor.findAll(spec);

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
