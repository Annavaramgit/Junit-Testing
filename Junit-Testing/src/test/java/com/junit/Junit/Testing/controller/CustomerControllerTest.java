package com.junit.Junit.Testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.Junit.Testing.entity.Customer;
import com.junit.Junit.Testing.service.CustomerService;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    //MockMvc is for testing controller endpoints (HTTP-like tests) without starting a server.
    @Autowired
    private MockMvc mockMvc;

    //for conversion (obj to json vice versa)
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerService customerService;

    Customer customer;

    /**
     * TestConfiguration is,
     * It is a special Spring Boot annotation used to define test-specific beans.
     * It works like @Configuration, but it's only applied during testing.
     */
    @TestConfiguration
    static class MockConfig {
        @Bean
        public CustomerService customerService() {
            return Mockito.mock(CustomerService.class);
        }
    }

    @BeforeEach
    public void setUpCustomer() {
        customer = new Customer();

        customer.setCustomerName("naveen");
        customer.setCustomerMobile("11111");
    }

    //save-happy
    @Test
    void saveCustomerShouldSaveSuccessfully() throws Exception {
        // Stub the service method
        Mockito.when(customerService.saveCustomer(Mockito.any(Customer.class)))
                .thenReturn(customer);

        // Act & Assert
        mockMvc.perform(
                        post("/junit-testing/save-customer")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(customer))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())))
                .andExpect(jsonPath("$.customerMobile", is(customer.getCustomerMobile())));
    }

    //save- unhappy
    @Test
    public void saveCustomerShouldFailOnNullBody() throws Exception {
        Mockito.when(customerService.saveCustomer(Mockito.any(Customer.class)))
                .thenReturn(null);

        mockMvc.perform(
                        post("/junit-testing/save-customer")
                                .contentType(MediaType.APPLICATION_JSON)
//                              .content(objectMapper.writeValueAsString(customer)) //or below line test will pass
                                .content("")
                ).andExpect(status().isBadRequest())
                .andDo(print());
    }

    //find customer by name - happy
    @Test
    public void findCustomerByNameShouldReturnSuccessfully() throws Exception {
        Mockito.when(customerService.findCustomerByName(customer.getCustomerName())).thenReturn(customer);

        mockMvc.perform(
                        get("/junit-testing/find-customer-by-name")
                                .param("customerName", customer.getCustomerName())
                                .contentType(MediaType.APPLICATION_JSON)

                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())))
                .andExpect(jsonPath("$.customerMobile", is(customer.getCustomerMobile())));
    }

    //find customer by name - unhappy
    @Test
    public void findCustomerByNameShouldReturnNoContent() throws Exception {
        //if we put this line in coment test will pass
        Mockito.when(customerService.findCustomerByName(null)).thenReturn(null);

        mockMvc.perform(
                        get("/junit-testing/find-customer-by-name")
                                .param("customerName", "")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }


    //fetch all customers - happy
    @Test
    public void fetchAllCustomersShouldReturnCustomersSuccessfully() throws Exception {

        List<Customer> listOfCustomers = new ArrayList<>();
        listOfCustomers.add(customer);

        customer = new Customer();
        customer.setCustomerName("rana");
        customer.setCustomerMobile("546546");

        listOfCustomers.add(customer);

        Mockito.when(customerService.fetchAllCustomers()).thenReturn(listOfCustomers);

        mockMvc.perform(
                        get("/junit-testing/fetch-all-customers"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(listOfCustomers.size())));
    }

    //fetch all customers - unhappy
    @Test
    public void fetchAllCustomersShouldReturnBadRequestOnEmptyList() throws Exception {
        Mockito.when(customerService.fetchAllCustomers()).thenReturn(new ArrayList<>());

        mockMvc.perform(
                        get("/junit-testing/fetch-all-customers"))
                .andExpect(status().isNoContent());
    }


    //delete customer by id - happy
    @Test
    public void deleteCustomerByIdShouldDeleteSuccessfully() throws Exception {

        Mockito.doNothing().when(customerService).deleteCustomerById(customer.getCustomerId());

        mockMvc.perform(
                        delete("/junit-testing/delete-customer-by-id")
                                .header("customerId", customer.getCustomerId())
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    //delete customer by id - happy
    @Test
    public void deleteCustomerByIdShouldFailWithoutHeader() throws Exception {
        mockMvc.perform(
                delete("/junit-testing/delete-customer-by-id")
        ).andExpect(status().isBadRequest());
    }


    //update customer - happy
    @Test
    public void updateCustomerInfoShouldUpdateSuccessfully() throws Exception {
        Mockito.when(customerService.updateCustomerDetails(customer.getCustomerId(), customer)).thenReturn(customer);

        customer.setCustomerName("mallika");

        mockMvc.perform(
                        patch("/junit-testing/update-customer/{customerId}", customer.getCustomerId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(customer))
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.customerName", is(customer.getCustomerName())));
    }


    //update customer - unhappy
    @Test
    public void updateCustomerInfoShouldFailOnEmptyBody() throws Exception {
        mockMvc.perform(
                patch("/junit-testing/update-customer/{customerId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("") // missing body
        ).andExpect(status().isBadRequest());
    }


}
