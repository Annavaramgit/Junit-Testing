package com.junit.Junit.Testing.service;

import com.junit.Junit.Testing.entity.Customer;
import com.junit.Junit.Testing.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


import java.lang.reflect.Field;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

@Service
@Slf4j
public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //save customer
    public Customer saveCustomer(Customer customer) {
//        log.info("saveCustomer() started");
        if (!ObjectUtils.isEmpty(customer)) {
            return customerRepository.save(customer);
        } else {
//            log.info("customer object is empty " + customer);
            return null;
        }
    }


    //delete customer by id
    public void deleteCustomerById(long id) {
//        log.info("deleteCustomerById() started ; id: " + id);
        if (id != 0) {
            customerRepository.deleteById(id);
        } else {
//            log.error("id has no value ; id " + id);
        }
    }


    public Customer findCustomerByName(String customerName) {
//        log.info("findCustomerByName() started ");

        if (!validateCustomerName(customerName)) {
            return customerRepository.findByCustomerName(customerName)
                    .orElseThrow(() -> new NoSuchElementException("Data With customer name not present; name: " + customerName));
        } else {
            throw new IllegalArgumentException("customer name invalid  , name: " + customerName);
        }


    }

    private boolean validateCustomerName(String customerName) {
//        log.info("customerName : {}", customerName);
//        log.info("validateCustomerName ; name is valid ? " + StringUtils.isEmpty(customerName));
        return StringUtils.isEmpty(customerName);
    }


    public List<Customer> fetchAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer updateCustomerDetails(long customerId, Customer customer) {
//        log.info("updateCustomerDetails started ; customerId :{} , customer:{}", customerId, customer);
        if (customerId != 0) {
            Optional<Customer> fetchedCustomer = customerRepository.findById(customerId);

            if (fetchedCustomer.isPresent()) {
//                log.info("customer info avaialble ");
                Customer resCustomer = fetchedCustomer.get();
                updateNotNullFeildsInRequestOnly(resCustomer, customer);
                return customerRepository.save(resCustomer);
            } else {
                throw new RuntimeException("Customer with : " + customerId + " not available !!");
            }
        } else {
            throw new RuntimeException("To update customer info customer id should ; " + customerId);
        }
    }

    /**
     * Here Password,Role those sensitive and secured fields not there so that's why directly non null
     * fields in request changing, but if those there we should not change directly those fields ,
     * we should skip(by using commented continue code below ) those fields changing ,
     * change those via email,otp etc...
     */
    private void updateNotNullFeildsInRequestOnly(Customer target, Customer source) {
//        log.info("updateNotNullFeildsInRequestOnly started ");

        Field[] declaredFields = source.getClass().getDeclaredFields();

        for (Field field : declaredFields) {
//            log.info("name : {} ", field.getName());
            try {
                field.setAccessible(true);
                if (field.getName().equalsIgnoreCase("customerId")) continue;
                //if (field.getName().equalsIgnoreCase("password")) continue;
                //if (field.getName().equalsIgnoreCase("role")) continue;
                Object value = field.get(source);
                if (!ObjectUtils.isEmpty(value)) {
                    field.set(target, value);
                }
            } catch (IllegalArgumentException e) {
//                log.error(" Error while update only not null values in the request {}: ", ExceptionUtils.getMessage(e));
                throw new RuntimeException("Error while update only not null values in the request " + ExceptionUtils.getMessage(e));

            } catch (Exception e) {
//                log.error("Error while update only not null values in the request : {}", ExceptionUtils.getMessage(e));
                throw new RuntimeException("Error while update only not null values in the request " + ExceptionUtils.getMessage(e));
            }
        }
    }

}
