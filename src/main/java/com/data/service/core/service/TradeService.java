package com.data.service.core.service;

import com.data.service.core.model.Trade;
import com.data.service.core.model.TradeEntity;
import com.data.service.core.repository.TradeRepository;
import com.data.service.core.mapper.TradeMapper;
import org.springframework.stereotype.Service;

@Service
public class TradeService extends GenericService<Trade, TradeEntity> {

    public TradeService(TradeRepository repository, TradeMapper mapper) {
        super(repository, repository, mapper, Trade.class);
    }
}
