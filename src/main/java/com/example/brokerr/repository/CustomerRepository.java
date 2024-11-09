package com.example.brokerr.repository;

import com.example.brokerr.model.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class CustomerRepository {

    private final EntityManager entityManager;

    @Autowired
    public CustomerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Tüm müşteri bilgilerini al
    public List<Customer> findAll() {
        String jpql = "SELECT c FROM Customer c";  // 'Customer' entity'si üzerinden tüm verileri al

        // JPQL sorgusuyla müşteri bilgilerini çek
        return entityManager.createQuery(jpql, Customer.class).getResultList();
    }

    // customerId ile müşteri bilgisi al
    public Customer findById(Long customerId) {
        String jpql = "SELECT c FROM Customer c WHERE c.id = :customerId";  // ID'ye göre müşteri arama

        try {
            return entityManager.createQuery(jpql, Customer.class)
                    .setParameter("customerId", customerId)
                    .getSingleResult();  // Tek bir sonuç döner, yoksa exception fırlatır
        } catch (NoResultException e) {
            return null;  // Müşteri bulunamazsa, null döndür
        }
    }

    public Customer findByName(String name) {
        String jpql = "SELECT c FROM Customer c WHERE c.name = :name";  // ID'ye göre müşteri arama

        try {
            return entityManager.createQuery(jpql, Customer.class)
                    .setParameter("name", name)
                    .getSingleResult();  // Tek bir sonuç döner, yoksa exception fırlatır
        } catch (NoResultException e) {
            return null;  // Müşteri bulunamazsa, null döndür
        }
    }

    // Müşteri ekleme (Insert işlemi)
    public void addCustomer(Customer customer) {
        entityManager.persist(customer);
    }

    public Long findMaxId() {
        String query = "SELECT COALESCE(MAX(e.id), -1) FROM Customer e";
        return (Long) entityManager.createQuery(query).getSingleResult();
    }

    @Transactional
    public void updateBalance(Long customerId, BigDecimal balance) {
        String jpql = "UPDATE Customer c SET c.balance = :balance WHERE c.id = :customerId";
        entityManager.createQuery(jpql)
                .setParameter("balance", balance)
                .setParameter("customerId", customerId)
                .executeUpdate();
    }

    @Transactional
    public void updateUsableBalance(Long customerId, BigDecimal usableBalance) {
        String jpql = "UPDATE Customer c SET c.usableBalance = :usableBalance WHERE c.id = :customerId";
        entityManager.createQuery(jpql)
                .setParameter("usableBalance", usableBalance)
                .setParameter("customerId", customerId)
                .executeUpdate();
    }
}
