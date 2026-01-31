package ${packageName};

import ${entityPackage}.${entityClass};
import org.springframework.stereotype.Component;

/**
 * Mapper Implementation.
 * You can customize logic here. This file will not be overwritten if it exists.
 */
@Component
public class ${className} extends ${baseClassName} {

    <% attributes.each { attr -> %>
    <% if (attr.isCalculated) { %>
    @Override
    protected ${attr.type} calculate${attr.capitalizedName}(${entityClass} entity) {
        // TODO: Implement custom logic for ${attr.name}
        return null; // Default value
    }
    <% } %>
    <% } %>
}
