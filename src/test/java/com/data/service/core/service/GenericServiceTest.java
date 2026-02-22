package com.data.service.core.service;

import com.data.service.core.mapper.EntityMapper;
import com.data.service.core.search.MetricRequest;
import com.data.service.core.search.SearchCriteria;
import com.data.service.core.search.SearchOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenericServiceTest {

    @Mock
    private JpaRepository<TestEntity, Long> repository;

    @Mock
    private JpaSpecificationExecutor<TestEntity> specExecutor;

    @Mock
    private EntityMapper<TestModel, TestEntity> mapper;

    private TestService testService;

    @BeforeEach
    void setUp() {
        testService = new TestService(repository, specExecutor, mapper);
    }

    @Test
    void testFindAll() {
        TestEntity entity = new TestEntity(1L, "value", 100.0);
        TestModel model = new TestModel(1L, "value", 100.0);

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        List<TestModel> result = testService.findAll();

        assertEquals(1, result.size());
        assertEquals(model, result.get(0));
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        TestEntity entity = new TestEntity(1L, "value", 100.0);
        TestModel model = new TestModel(1L, "value", 100.0);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toModel(entity)).thenReturn(model);

        TestModel result = testService.findById(1L);

        assertEquals(model, result);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        TestEntity entity = new TestEntity(1L, "value", 100.0);
        TestModel model = new TestModel(1L, "value", 100.0);

        when(mapper.toEntity(model)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toModel(entity)).thenReturn(model);

        TestModel result = testService.save(model);

        assertEquals(model, result);
        verify(repository, times(1)).save(entity);
    }

    @Test
    void testDeleteById() {
        testService.deleteById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetMetricCount() {
        MetricRequest request = new MetricRequest();
        request.setMetricType("COUNT");
        request.setFilters(
                Collections.singletonList(new SearchCriteria("someField", SearchOperation.EQUALITY, "value")));

        when(specExecutor.count(any())).thenReturn(5L);

        Object result = testService.getMetric(request);

        assertEquals(5L, result);
        verify(specExecutor, times(1)).count(any());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGetMetricSum() {
        MetricRequest request = new MetricRequest();
        request.setMetricType("SUM");
        request.setField("doubleField");

        TestEntity e1 = new TestEntity(1L, "val1", 50.0);
        TestEntity e2 = new TestEntity(2L, "val2", 25.5);

        when(specExecutor.findAll(nullable(Specification.class))).thenReturn(List.of(e1, e2));

        Object result = testService.getMetric(request);

        assertEquals(75.5, result);
        verify(specExecutor, times(1)).findAll(nullable(Specification.class));
    }

    static class TestModel {
        Long id;
        String stringField;
        Double doubleField;

        TestModel(Long id, String stringField, Double doubleField) {
            this.id = id;
            this.stringField = stringField;
            this.doubleField = doubleField;
        }
    }

    static class TestEntity {
        Long id;
        String stringField;
        Double doubleField;

        TestEntity(Long id, String stringField, Double doubleField) {
            this.id = id;
            this.stringField = stringField;
            this.doubleField = doubleField;
        }
    }

    static class TestService extends GenericService<TestModel, TestEntity> {
        public TestService(JpaRepository<TestEntity, Long> repository,
                JpaSpecificationExecutor<TestEntity> specExecutor,
                EntityMapper<TestModel, TestEntity> mapper) {
            super(repository, specExecutor, mapper);
        }
    }
}
