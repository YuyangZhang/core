package com.data.service.core.service;

import com.data.service.core.model.Trade;
import com.data.service.core.repository.TradeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    public List<Trade> getTrades(String tradeType, LocalDate tradeDate) {
        return tradeRepository.findByTradeTypeAndTradeDate(tradeType, tradeDate);
    }

    public Trade createTrade(Trade trade) {
        return tradeRepository.save(trade);
    }

}
