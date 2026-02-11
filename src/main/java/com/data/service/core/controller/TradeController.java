package com.data.service.core.controller;

import com.data.service.core.model.Trade;
import com.data.service.core.model.TradeEntity;
import com.data.service.core.service.TradeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trades")
public class TradeController extends GenericController<Trade, TradeEntity> {

    public TradeController(TradeService service) {
        super(service);
    }
}
