package com.data.service.core.search;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;

public class GenericSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    public GenericSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        String key = criteria.getKey();
        Object value = criteria.getValue();
        SearchOperation op = criteria.getOperation();

        // Handle LocalDate parsing if needed
        if (root.get(key).getJavaType() == LocalDate.class && value instanceof String) {
            value = LocalDate.parse((String) value);
        }

        switch (op) {
            case EQUALITY:
                return builder.equal(root.get(key), value);
            case NEGATION:
                return builder.notEqual(root.get(key), value);
            case GREATER_THAN:
                if (value instanceof LocalDate) {
                    return builder.greaterThan(root.get(key), (LocalDate) value);
                }
                if (value instanceof Number) {
                    return builder.gt(root.get(key).as(Double.class), ((Number) value).doubleValue());
                }
                return builder.greaterThan(root.get(key), value.toString());
            case LESS_THAN:
                if (value instanceof LocalDate) {
                    return builder.lessThan(root.get(key), (LocalDate) value);
                }
                if (value instanceof Number) {
                    return builder.lt(root.get(key).as(Double.class), ((Number) value).doubleValue());
                }
                return builder.lessThan(root.get(key), value.toString());
            case LIKE:
                return builder.like(root.get(key), "%" + value + "%");
            case STARTS_WITH:
                return builder.like(root.get(key), value + "%");
            case ENDS_WITH:
                return builder.like(root.get(key), "%" + value);
            case CONTAINS:
                return builder.like(root.get(key), "%" + value + "%");
            default:
                return null;
        }
    }
}
