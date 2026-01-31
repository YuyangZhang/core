package com.data.service.core.repository;

import com.data.service.core.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByTradeTypeAndTradeDate(String tradeType, LocalDate tradeDate);
}
