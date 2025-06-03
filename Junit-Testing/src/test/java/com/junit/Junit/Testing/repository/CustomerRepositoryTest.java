package com.junit.Junit.Testing.repository;

import com.junit.Junit.Testing.entity.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Customer customer1;
    private Customer customer2;

    @BeforeEach
    public void setUp() {

        customer1 = new Customer();
        customer1.setCustomerName("one");
        customer1.setCustomerMobile("11111");

        customer2 = new Customer();

        customer2.setCustomerName("two");
        customer2.setCustomerMobile("2222");

        System.out.println("---customer 1: " + customer1 + " --- customer 2 :" + customer2);

        testEntityManager.persist(customer1);
        testEntityManager.persist(customer2);
    }
/*
    @Test
    @Order(1)
    public void saveCustomerTest() {
        // Arrange
        Customer customer = new Customer();
        customer.setCustomerName("raju");
        customer.setCustomerMobile("555555");

        Customer savedCustomer = customerRepository.save(customer);

        // Assert
        System.out.println("**********************************" + savedCustomer);
        Assertions.assertThat(savedCustomer).isNotNull();
        Assertions.assertThat(savedCustomer.getCustomerId()).isGreaterThan(0);
    }
 */

/*
    @Test
    @Order(2)
    public void fetchAllCustomers(){

        List<Customer> allCustomers = customerRepository.findAll();

        System.out.println("allCustomers : "+ allCustomers);
        Assertions.assertThat(allCustomers.size()).isGreaterThan(0);
    }
*/

    @Test
    public void whenFindByName_thenReturnCustomer() {
        String customerName = "one";
        Optional<Customer> byCustomerName = customerRepository.findByCustomerName(customerName);

        Assertions.assertThat(byCustomerName).isPresent();
        Assertions.assertThat(byCustomerName.get().getCustomerName()).isEqualTo(customerName);
    }

    @Test
    public void whenFindByName_withNonExistingName_thenReturnEmpty(){
        String customerName = "one1";
        Optional<Customer> byCustomerName = customerRepository.findByCustomerName(customerName);

        Assertions.assertThat(byCustomerName).isNotPresent();
    }

    @Test
    public void whenFindAll_thenReturnAllCustomer() {

        List<Customer> all = customerRepository.findAll();
        System.out.println("result all customers : "+all);
        Assertions.assertThat(all.size()).isGreaterThan(0);
    }

    @Test
    public void whenDeleteById_withExistingCustomer_thenCustomerIsDeleted() {
        Long id = 10L;

        // when
        customerRepository.deleteById(id);

        // then
        Optional<Customer> deletedCustomer = customerRepository.findById(id);
        Assertions.assertThat(deletedCustomer).isNotPresent(); // or isEmpty()
    }


}
