package ${packageName};

import ${modelPackage}.${entityName};
import ${entityPackage}.${entityName}Entity;
import ${repositoryPackage}.${repositoryName};
import ${mapperPackage}.${mapperName};
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
public class ${className} {

    private final ${repositoryName} repository;
    private final ${mapperName} mapper;

    public List<${entityName}> findAll() {
        return repository.findAll().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    public ${entityName} save(${entityName} model) {
        ${entityName}Entity entity = mapper.toEntity(model);
        ${entityName}Entity saved = repository.save(entity);
        return mapper.toModel(saved);
    }

    public ${entityName} findById(Long id) {
        return repository.findById(id)
                .map(mapper::toModel)
                .orElse(null);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Object getMetric(MetricRequest request) {
        Specification<${entityName}Entity> spec = null;
        if (request.getFilters() != null) {
            for (SearchCriteria criteria : request.getFilters()) {
                Specification<${entityName}Entity> nextSpec = new GenericSpecification<>(criteria);
                spec = (spec == null) ? nextSpec : spec.and(nextSpec);
            }
        }

        if ("COUNT".equalsIgnoreCase(request.getMetricType())) {
            return repository.count(spec);
        }

        List<${entityName}Entity> entities = repository.findAll(spec);

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
