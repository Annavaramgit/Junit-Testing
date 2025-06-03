package com.junit.Junit.Testing.service;

import com.junit.Junit.Testing.entity.Customer;
import com.junit.Junit.Testing.repository.CustomerRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    /*
        // BeforeAll, BeforeEach, AfterAll, AfterEach these are methods of UnitTest lifeCycle
        //before all test methods it executes once
        @BeforeAll
        public static void checkBeforeAll() {
            System.out.println("This is BeforeAll !!");
        }

        //before each test method it executes
        @BeforeEach
        public void checkBeforeEach() {
            System.out.println("This is checkBeforeEach !!");
        }

        //afterall test methods it executes
        @AfterAll
        public static void checkAfterAll() {
            System.out.println("This is AfterAll !!");
        }

        //aftereach test method it executes
        @AfterEach
        public void checkAfterEach() {
            System.out.println("This is AfterEach !!");
        }
    */

    @Test
        //test saveCustomer
    void saveCustomerShouldSaveCustomerSuccessFully() {
        Customer customer = new Customer();

        customer.setCustomerId(301);
        customer.setCustomerName("mahi");
        customer.setCustomerMobile("7777777");


        /** This is whenever save() invoke then return product obj,
         * bcz we are not  originally saving in db ,
         * if original saving after save it will get some response(customer obj) as return,
         * here some it is dummy calling so we also require response as return
         */
        Mockito.when(customerRepository.save(customer)).thenReturn(customer);

        //actually method of saving customer in service cls invoking
        Customer savedCustomer = customerService.saveCustomer(customer);

        //testing -> bellow conditions satisfies the test pass otherwise test fails
        Assertions.assertNotNull(savedCustomer); //notnull
        Assertions.assertEquals(customer.getCustomerName(), savedCustomer.getCustomerName()); // both should equal
        Assertions.assertEquals(customer.getCustomerMobile(), savedCustomer.getCustomerMobile());  // both should equal

    }


    /**
     * testing void method using doNuthing().
     * in above we did test for save(), after saves it return object then we can test like compare provided data==return data.
     * but if void method checking :-
     * use doNothing() when particular method invokes and,
     * verify in the particular repository particular method particular no.of times invoked or not by using verify()
     */
    @Test
    //test DeleteCustomerById
    public void deleteCustomerByIdShouldDeleteCustomer() {
        // mock behavior: do nothing when deleteById is called
        Mockito.doNothing().when(customerRepository).deleteById(1L);

        // call the method
        customerService.deleteCustomerById(1);

        // verify it was called once
        Mockito.verify(customerRepository, Mockito.times(1)).deleteById(1L);
    }


    /**
     * test private methods directly not recommended , test public methods that internally used private methods.
     * we can test private methods by using java reflection , by using getDeclaredMethod() in reflection package.
     */
    //test private method ( test scenario if name valid , here validCustomerName() is private method)
//    @Test
//    public void testPrivateMethod_validateCustomerNameIfNameValid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Method validateCustomerName = CustomerService.class.getDeclaredMethod("validateCustomerName", String.class);
//        validateCustomerName.setAccessible(true);
//
//        Boolean customerName = (Boolean) validateCustomerName.invoke(customerService, "mahi");
//        Assertions.assertTrue(customerName);
//    }
//
//    //test private method ( test scenario if name not valid, here validCustomerName() is private method)
//    @Test
//    public void testPrivateMethod_validateCustomerNameIfNotNameValid() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Method validateCustomerName = CustomerService.class.getDeclaredMethod("validateCustomerName", String.class);
//        validateCustomerName.setAccessible(true);
//
//        Boolean customerName = (Boolean) validateCustomerName.invoke(customerService, " ");
//        Assertions.assertFalse(customerName);
//    }

    /**
     * below three test for findCustomerByName(). provide test for exceptional scenario's
     * one is should return customer -> success path.
     * second is when name is null || empty || whitespace, then illegalargument should raise.
     * third is when provided name is not present in db then nosuchelement should raise.
     */
    //test find customer by name should give customer info
    @Test
    public void findCustomerByNameShouldReturnCustomerSuccessfully() {
        String customerName = "mahi";
        Customer customer = new Customer(10L, customerName, "5585");

        Mockito.when(customerRepository.findByCustomerName(customerName)).thenReturn(Optional.of(customer));

        Customer resultCustomer = customerService.findCustomerByName(customerName);

        Assertions.assertNotNull(resultCustomer);
        Assertions.assertEquals(customerName, resultCustomer.getCustomerName());
        Assertions.assertEquals(customer.getCustomerMobile(), resultCustomer.getCustomerMobile());

    }

    //test find customer by name should throw Illegal when name invalid(""," ",null)
    @Test
    public void findCustomerByNameShouldThrowIllegalArgumentExceptionForInvalidName() {
        String customerName = "";

        //when findCustomerByName() invoking time if IllegalArgumentException then catch it
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            customerService.findCustomerByName(customerName);
        });

        //here  reason of exception and with this static provided err msg are same or not if yes test pass or fail
        Assertions.assertTrue(exception.getMessage().contains("customer name invalid"));

        //when exception means save() should not invoke here checking is it invoking zero times, if yes (0 times) then pass or else fail.
        Mockito.verify(customerRepository, Mockito.times(0)).save(Mockito.any(Customer.class));

    }

    //test find customer by name should throw NoSuchElement  when name not found in db
    @Test
    public void findCustomerByNameShouldThrowNoSuchElementExceptionWhenCustomerNotFound() {
        String customerName = "h";

        Mockito.when(customerRepository.findByCustomerName(customerName)).thenReturn(Optional.empty());

        //when findCustomerByName invoke times if nosuchelementexception catch it
        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class, () -> {
            customerService.findCustomerByName(customerName);
        });

        //here  reason  of exception and with this static provided are same or not if yes test pass
        Assertions.assertTrue(exception.getMessage().contains("Data With customer name not present"));
        //when exception means save() should not invoke here checking is it invoking zero times, if yes (0 times) then pass or else fail.
        Mockito.verify(customerRepository, Mockito.times(0)).save(Mockito.any(Customer.class));
    }



}