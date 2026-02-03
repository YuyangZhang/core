package ${packageName};

import ${modelPackage}.${entityName};
import ${servicePackage}.${serviceName};
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.data.service.core.search.MetricRequest;

@RestController
@RequestMapping("/api/${entityName.toLowerCase()}s")
@RequiredArgsConstructor
public class ${className} {

    private final ${serviceName} service;

    @GetMapping
    public List<${entityName}> getAll() {
        return service.findAll();
    }

    @PostMapping
    public ${entityName} create(@RequestBody ${entityName} entity) {
        return service.save(entity);
    }

    @GetMapping("/{id}")
    public ResponseEntity<${entityName}> getById(@PathVariable Long id) {
        ${entityName} entity = service.findById(id);
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
