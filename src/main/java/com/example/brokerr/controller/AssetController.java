package com.example.brokerr.controller;

import com.example.brokerr.service.AssetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.brokerr.model.Asset;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    // Müşteri ID'sine göre varlıkları al
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Asset>> getAssetsByCustomerId(@PathVariable Long customerId) {
        try {
            List<Asset> assets = assetService.getAssetsByCustomerId(customerId);
            return ResponseEntity.ok(assets);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Asset adı ve müşteri ID'sine göre varlık al
    @GetMapping("/customer/{customerId}/asset/{assetName}")
    public Asset getAssetByCustomerAndAssetName(@PathVariable Long customerId, @PathVariable String assetName) {
        try {
            Asset asset = assetService.getAssetByCustomerAndAssetName(customerId, assetName);
            return asset;
        } catch (Exception e) {
            return null;
        }
    }
}

