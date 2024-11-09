package com.example.brokerr.repository;

import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.example.brokerr.model.Asset;

@Repository
public class AssetRepository {

    private final EntityManager entityManager;

    public AssetRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Yeni Asset kaydetme
    public void saveAsset(Asset asset) {
        if (asset.getId() == null) {
            entityManager.persist(asset);  // Yeni bir Asset ekliyoruz
        } else {
            entityManager.merge(asset);  // Var olan bir Asset'i güncelliyoruz
        }
    }

    // Müşteri ID'sine göre varlıkları al
    public List<Asset> getAssetsByCustomerId(Long customerId) {
        String query = "SELECT a FROM Asset a WHERE a.customerId = :customerId";
        TypedQuery<Asset> typedQuery = entityManager.createQuery(query, Asset.class);
        typedQuery.setParameter("customerId", customerId);
        return typedQuery.getResultList();
    }

    // Asset adı ile varlık alma (Örnek: TRY varlığı)
    public Asset getAssetByCustomerAndAssetName(Long customerId, String assetName) {
        String query = "SELECT a FROM Asset a WHERE a.customerId = :customerId AND a.assetName = :assetName";
        TypedQuery<Asset> typedQuery = entityManager.createQuery(query, Asset.class);
        typedQuery.setParameter("customerId", customerId);
        typedQuery.setParameter("assetName", assetName);
        try {
            return typedQuery.getSingleResult();
        } catch (Exception e) {
            return null;  // Varlık bulunamazsa boş döner
        }
    }
@Transactional
    public void addOrUpdateAsset(Long customerId, Long assetId, BigDecimal size, BigDecimal usableSize, LocalDateTime lastUpdateTime, String stockCode, String orderSide) {

        String query = "SELECT COALESCE(MAX(e.id), -1) FROM Asset e";
        Long maxId= (Long) entityManager.createQuery(query).getSingleResult();
        Long id = (maxId == -1) ? 0 : maxId + 1;
        String selectQuery = "SELECT a FROM Asset a WHERE a.customerId = :customerId AND a.assetName = :stockCode";
        TypedQuery<Asset> typedQuery = entityManager.createQuery(selectQuery, Asset.class);
        typedQuery.setParameter("customerId", customerId);
        typedQuery.setParameter("stockCode", stockCode);

        try {
            // Update existing asset
            Asset existingAsset = typedQuery.getSingleResult();
            if(orderSide.equals("BUY")) {
                existingAsset.setSize(existingAsset.getSize().add(size));
                existingAsset.setUsableSize(existingAsset.getUsableSize().add(usableSize));
            } else {
                existingAsset.setSize(existingAsset.getSize().subtract(size));
                existingAsset.setUsableSize(existingAsset.getUsableSize().subtract(usableSize));
            }
            existingAsset.setLastUpdateTime(lastUpdateTime);
            existingAsset.setAssetName(stockCode);
            entityManager.merge(existingAsset);
        } catch (NoResultException e) {
            // Create and persist new asset if not found
            Asset newAsset = new Asset();
            newAsset.setCustomerId(customerId);
            newAsset.setId(id);
            newAsset.setSize(size);
            newAsset.setUsableSize(usableSize);
            newAsset.setLastUpdateTime(lastUpdateTime);
            newAsset.setAssetName(stockCode);
            entityManager.persist(newAsset);
        }
    }

}
