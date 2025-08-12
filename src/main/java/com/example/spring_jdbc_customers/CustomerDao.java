package com.example.spring_jdbc_customers;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> findAll();
    Optional<Customer> findById(Long id);
    Long create(Customer customer);
    int update(Customer customer);
    int delete(Long id);
}