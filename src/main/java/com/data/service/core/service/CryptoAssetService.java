package com.data.service.core.service;

import com.data.service.core.model.CryptoAsset;
import com.data.service.core.model.CryptoAssetEntity;
import com.data.service.core.repository.CryptoAssetRepository;
import com.data.service.core.mapper.CryptoAssetMapper;
import org.springframework.stereotype.Service;

@Service
public class CryptoAssetService extends GenericService<CryptoAsset, CryptoAssetEntity> {

    public CryptoAssetService(CryptoAssetRepository repository, CryptoAssetMapper mapper) {
        super(repository, repository, mapper);
    }
}
