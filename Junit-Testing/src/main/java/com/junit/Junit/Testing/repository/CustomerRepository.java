package com.junit.Junit.Testing.repository;

import com.junit.Junit.Testing.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    public Optional<Customer> findByCustomerName(String customerName);
    public Optional<Customer> findByCustomerId(long customerId);
}
