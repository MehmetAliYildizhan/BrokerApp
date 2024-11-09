package com.example.brokerr.repository;

import com.example.brokerr.model.Orders;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public class OrderRepository {

    @Autowired
    private EntityManager entityManager;

    public Long findMaxId() {
        String query = "SELECT COALESCE(MAX(e.id), -1) FROM Orders e";
        return (Long) entityManager.createQuery(query).getSingleResult();
    }

    // Create a new order
    @Transactional
    public void createOrder(Orders order
            //Long customerId, Long assetId, String orderSide, BigDecimal size, BigDecimal price, String status, LocalDateTime createDate
    ) {
        /*String query = "INSERT INTO ORDERS (customer_id, asset_id, order_side, size, price, status, create_date) " +
                "VALUES (:customerId, :assetId, :orderSide, :size, :price, :status, :createDate)";
        entityManager.createNativeQuery(query)
                .setParameter("customerId", customerId)
                .setParameter("assetId", assetId)
                .setParameter("orderSide", orderSide)
                .setParameter("size", size)
                .setParameter("price", price)
                .setParameter("status", status)
                .setParameter("createDate", createDate)
                .executeUpdate();*/
        entityManager.persist(order);
    }

    // Find order by ID
    public Orders findById(Long orderId) {
        String query = "SELECT o FROM Orders o WHERE o.id = :orderId";
        TypedQuery<Orders> typedQuery = entityManager.createQuery(query, Orders.class);
        typedQuery.setParameter("orderId", orderId);
        Orders ord = typedQuery.getSingleResult();
        try {
            return ord;
        } catch (NoResultException e) {
            return null;
        }
    }

    // Update the order status
    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        String query = "UPDATE Orders o SET o.status = :status WHERE o.id = :orderId";
        entityManager.createQuery(query)
                .setParameter("status", status)
                .setParameter("orderId", orderId)
                .executeUpdate();
    }

    // Cancel the order by setting status to 'CANCELED'
    public void cancelOrder(Long orderId) {
        String query = "UPDATE ORDERS o SET o.status = 'CANCELED' WHERE o.id = :orderId";
        entityManager.createQuery(query)
                .setParameter("orderId", orderId)
                .executeUpdate();
    }

    // Find orders by status (e.g., PENDING, MATCHED, CANCELED)
    public List<Orders> findByStatus(String status) {
        String query = "SELECT o FROM ORDERS o WHERE o.status = :status";
        TypedQuery<Orders> typedQuery = entityManager.createQuery(query, Orders.class);
        typedQuery.setParameter("status", status);
        return typedQuery.getResultList();
    }

    // Find orders by customer ID
    public List<Orders> findByCustomerId(Long customerId) {
        String query = "SELECT o FROM ORDERS o WHERE o.customerId = :customerId";
        TypedQuery<Orders> typedQuery = entityManager.createQuery(query, Orders.class);
        typedQuery.setParameter("customerId", customerId);
        return typedQuery.getResultList();
    }

    // Find orders by order side (BUY or SELL)
    public List<Orders> findByOrderSide(String orderSide) {
        String query = "SELECT o FROM ORDERS o WHERE o.orderSide = :orderSide";
        TypedQuery<Orders> typedQuery = entityManager.createQuery(query, Orders.class);
        typedQuery.setParameter("orderSide", orderSide);
        return typedQuery.getResultList();
    }

    // Find orders by customer ID and status (for filtering pending orders for a customer, for example)
    public List<Orders> findByCustomerIdAndStatus(Long customerId, String status) {
        String query = "SELECT o FROM ORDERS o WHERE o.customerId = :customerId AND o.status = :status";
        TypedQuery<Orders> typedQuery = entityManager.createQuery(query, Orders.class);
        typedQuery.setParameter("customerId", customerId);
        typedQuery.setParameter("status", status);
        return typedQuery.getResultList();
    }

    // Find orders by customer ID, order side (BUY/SELL) and status
    public List<Orders> findByCustomerIdOrderSideAndStatus(Long customerId, String orderSide, String status) {
        String query = "SELECT o FROM ORDERS o WHERE o.customerId = :customerId AND o.orderSide = :orderSide AND o.status = :status";
        TypedQuery<Orders> typedQuery = entityManager.createQuery(query, Orders.class);
        typedQuery.setParameter("customerId", customerId);
        typedQuery.setParameter("orderSide", orderSide);
        typedQuery.setParameter("status", status);
        return typedQuery.getResultList();
    }

    // Find orders within a price range
    public List<Orders> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        String query = "SELECT o FROM ORDERS o WHERE o.price BETWEEN :minPrice AND :maxPrice";
        TypedQuery<Orders> typedQuery = entityManager.createQuery(query, Orders.class);
        typedQuery.setParameter("minPrice", minPrice);
        typedQuery.setParameter("maxPrice", maxPrice);
        return typedQuery.getResultList();
    }

    // Find orders by assetId (e.g., a specific stock's orders)
    public List<Orders> findByAssetId(Long assetId) {
        String query = "SELECT o FROM ORDERS o WHERE o.assetId = :assetId";
        TypedQuery<Orders> typedQuery = entityManager.createQuery(query, Orders.class);
        typedQuery.setParameter("assetId", assetId);
        return typedQuery.getResultList();
    }

    // Find orders by date range (created within specific dates)
    public List<Orders> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String query = "SELECT o FROM ORDERS o WHERE o.createDate BETWEEN :startDate AND :endDate";
        TypedQuery<Orders> typedQuery = entityManager.createQuery(query, Orders.class);
        typedQuery.setParameter("startDate", startDate);
        typedQuery.setParameter("endDate", endDate);
        return typedQuery.getResultList();
    }
}
