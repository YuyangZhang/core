package ${packageName};

import ${modelPackage}.${entityName};
import ${modelPackage}.${entityName}Entity;
import ${servicePackage}.${serviceName};
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/${entityName.toLowerCase()}s")
public class ${className} extends GenericController<${entityName}, ${entityName}Entity> {

    public ${className}(${serviceName} service) {
        super(service);
    }
}
