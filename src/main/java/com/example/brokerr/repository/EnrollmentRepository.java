package com.example.brokerr.repository;

import com.example.brokerr.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrollmentRepository{
    @PersistenceContext
    private EntityManager entityManager;

    // En son ID'yi döndüren özel metod
    public Long findMaxId() {
        String query = "SELECT COALESCE(MAX(e.id), -1) FROM Employee e";
        return (Long) entityManager.createQuery(query).getSingleResult();
    }

    // CRUD işlemleri
    public Employee save(Employee employee) {
        if (employee.getId() != null && entityManager.contains(employee)) {
            return entityManager.merge(employee);
        } else {
            entityManager.persist(employee);
            return employee;
        }
    }

    public List<Employee> getAllEmployees() {
        String query = "SELECT e FROM Employee e";  // JPQL sorgusu
        TypedQuery<Employee> typedQuery = entityManager.createQuery(query, Employee.class);
        return typedQuery.getResultList();  // Sonuç listesini döner
    }

    public Employee getById(Long id) {
        return entityManager.find(Employee.class, id); // Verilen ID'ye göre çalışanı bul
    }

    // Kullanıcı adına göre çalışanı getirir
    public Employee getByUsername(String username) {
        String query = "SELECT e FROM Employee e WHERE e.username = :username"; // JPQL sorgusu
        TypedQuery<Employee> typedQuery = entityManager.createQuery(query, Employee.class);
        typedQuery.setParameter("username", username); // Parametre ayarlama
        Employee emp = new Employee(Long.valueOf(-1),"","");
        emp.setEmploymentType("");
        try {
            return typedQuery.getSingleResult(); // Sonuç döner
        } catch (NoResultException e) {
            return emp;
        } catch (Exception e) {
            // Diğer olası hataları loglayabiliriz
            e.printStackTrace();
            return emp;
        } // Sonuç döner
    }



    public void deleteById(Long id) {
        Employee employee = entityManager.find(Employee.class, id); // Silinecek çalışanı bul
        if (employee != null) {
            entityManager.remove(employee); // Çalışanı sil
        }
    }
}
