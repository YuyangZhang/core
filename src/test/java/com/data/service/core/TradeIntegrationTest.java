package com.data.service.core;

import com.data.service.core.model.Trade;
import com.data.service.core.repository.TradeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TradeIntegrationTest {

    @Autowired
    TradeRepository tradeRepository;

    @Test
    void testTradesAreLoaded() {
        List<Trade> trades = tradeRepository.findAll();
        assertThat(trades).hasSize(2);

        Trade trade = trades.stream().filter(t -> t.getTradeType().equals("SPOT")).findFirst().orElseThrow();
        assertThat(trade.getCurrency()).isEqualTo("USD");
        assertThat(trade.getAmount()).isEqualTo(1000.0);
    }
}
