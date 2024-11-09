package com.example.brokerr.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.example.brokerr.model.Asset;

import java.util.List;

@Repository
public class TransactionRepository {

    @Autowired
    private EntityManager entityManager;

    // Customer ID'sine göre varlıkları getir
    public List<Asset> getAssetsByCustomerId(Long customerId) {
        String query = "SELECT ca FROM ASSET ca WHERE ca.customer.id = :customerId";
        TypedQuery<Asset> typedQuery = entityManager.createQuery(query, Asset.class);
        typedQuery.setParameter("customerId", customerId);
        return typedQuery.getResultList();
    }

    // Müşteriye varlık ekle (Deposit işlemi)
    @Transactional
    public void deposit(Long customerId, String stockName, Long quantity) {
        String query = "UPDATE CustomerAssets ca SET ca.size = ca.size + :quantity " +
                "WHERE ca.customer.id = :customerId AND ca.stockName = :stockName";
        int updated = entityManager.createQuery(query)
                .setParameter("customerId", customerId)
                .setParameter("stockName", stockName)
                .setParameter("quantity", quantity)
                .executeUpdate();

        if (updated == 0) {
            throw new RuntimeException("Asset not found for deposit.");
        }
    }

    // Müşteriden varlık çek (Withdraw işlemi)
    @Transactional
    public void withdraw(Long customerId, String stockName, Long quantity) {
        String query = "UPDATE CustomerAssets ca SET ca.size = ca.size - :quantity " +
                "WHERE ca.customer.id = :customerId AND ca.stockName = :stockName AND ca.size >= :quantity";
        int updated = entityManager.createQuery(query)
                .setParameter("customerId", customerId)
                .setParameter("stockName", stockName)
                .setParameter("quantity", quantity)
                .executeUpdate();

        if (updated == 0) {
            throw new RuntimeException("Insufficient assets or asset not found.");
        }
    }

    // Genel tüm varlıkları (Stock) al
    public List<Asset> getAllStocks() {
        String query = "SELECT s FROM ASSET s";
        return entityManager.createQuery(query, Asset.class).getResultList();
    }
}
