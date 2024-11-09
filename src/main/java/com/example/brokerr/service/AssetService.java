package com.example.brokerr.service;

import com.example.brokerr.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.brokerr.model.Asset;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AssetService {


    @Autowired
    private AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    // Müşteri ID'sine göre varlıkları al
    public List<Asset> getAssetsByCustomerId(Long customerId) {
        return assetRepository.getAssetsByCustomerId(customerId);
    }

    // Müşteri ID'si ve varlık adı ile varlık al
    public Asset getAssetByCustomerAndAssetName(Long customerId, String assetName) {
        return assetRepository.getAssetByCustomerAndAssetName(customerId, assetName);
    }

    // Asset kaydetme
    public void saveAsset(Asset asset) {
        assetRepository.saveAsset(asset);
    }

    public void addAsset(Long customerId, Long assetId, BigDecimal size, String stockCode, String orderSide) {
        assetRepository.addOrUpdateAsset(customerId, assetId, size, size, LocalDateTime.now(), stockCode,orderSide);
    }
}
