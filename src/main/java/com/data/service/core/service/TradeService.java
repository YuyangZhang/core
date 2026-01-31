package com.data.service.core.service;

import com.data.service.core.model.Trade;
import com.data.service.core.model.TradeEntity;
import com.data.service.core.repository.TradeRepository;
import com.data.service.core.mapper.TradeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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
}
