package com.junit.Junit.Testing.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long customerId;
    private String customerName;
    private String customerMobile;
/*
    public long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public Customer() {
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", customerMobile='" + customerMobile + '\'' +
                '}';
    }

    public Customer(long customerId, String customerName, String customerMobile) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerMobile = customerMobile;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }*/


}
