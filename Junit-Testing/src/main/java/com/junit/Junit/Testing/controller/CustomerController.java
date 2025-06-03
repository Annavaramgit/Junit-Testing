package com.junit.Junit.Testing.controller;

import com.junit.Junit.Testing.entity.Customer;
import com.junit.Junit.Testing.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/junit-testing")
public class CustomerController {


    private CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/save-customer")
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {
//        log.info("saveCustomer() started");
//        log.info("customer: {}", customer.getCustomerName());
        Customer resCustomer =  customerService.saveCustomer(customer);
        if(!ObjectUtils.isEmpty(resCustomer))
            return new ResponseEntity<>(resCustomer,HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete-customer-by-id")
    public void deleteCustomerById(@RequestHeader("customerId") long customerId) {
//        log.info("deleteCustomerById started");
        customerService.deleteCustomerById(customerId);
    }

    @GetMapping("/find-customer-by-name")
    public ResponseEntity<Optional<Customer>> findCustomerByName(@RequestParam("customerName") String customerName) {
//        log.info("findCustomerByName() started - name : {}",customerName);
        Customer cus = customerService.findCustomerByName(customerName);
//        log.info("in findCustomerByName() after hit service ; result customerInfo :{}",cus);
        if (!ObjectUtils.isEmpty(cus))
            return new ResponseEntity<>(Optional.of(cus), HttpStatus.OK);
         else
             return new ResponseEntity<>(Optional.empty(), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/fetch-all-customers")
    public ResponseEntity<List<Customer>> fetchAllCustomers() {

        List<Customer> fetchedCustomers = customerService.fetchAllCustomers();
        if (!CollectionUtils.isEmpty(fetchedCustomers)) {
            return new ResponseEntity<>(fetchedCustomers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

    }

    @PatchMapping("/update-customer/{customerId}")
    public ResponseEntity<Customer> updateCustomerInfo(
            @RequestBody Customer customer, @PathVariable("customerId") long customerId) {

        Customer resCustomer = customerService.updateCustomerDetails(customerId, customer);
//        log.info("in updateCustomerInfo() after hit service ; result customerInfo :{}",resCustomer);
        if (!ObjectUtils.isEmpty(resCustomer)) {
            return new ResponseEntity<>(resCustomer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

    }

}
