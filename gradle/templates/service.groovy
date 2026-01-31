package ${packageName};

import ${modelPackage}.${entityName};
import ${entityPackage}.${entityName}Entity;
import ${repositoryPackage}.${repositoryName};
import ${mapperPackage}.${mapperName};
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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
}
