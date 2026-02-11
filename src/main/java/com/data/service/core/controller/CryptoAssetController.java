package com.data.service.core.controller;

import com.data.service.core.model.CryptoAsset;
import com.data.service.core.model.CryptoAssetEntity;
import com.data.service.core.service.CryptoAssetService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cryptoassets")
public class CryptoAssetController extends GenericController<CryptoAsset, CryptoAssetEntity> {

    public CryptoAssetController(CryptoAssetService service) {
        super(service);
    }
}
