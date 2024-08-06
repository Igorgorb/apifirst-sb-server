package guru.springframework.apifirst.apifirstserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.apifirst.apifirstserver.domain.Product;
import guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import guru.springframework.apifirst.apifirstserver.repositories.OrderRepository;
import guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import guru.springframework.apifirst.model.CustomerDto;
import guru.springframework.apifirst.model.OrderDto;
import guru.springframework.apifirst.model.ProductDto;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class BaseTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    Filter validationFilter;

    @Autowired
    ObjectMapper objectMapper;

    public MockMvc mockMvc;

    CustomerDto testCustomer;
    Product testProduct;
    OrderDto testOrder;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(validationFilter)
                .build();

//        testCustomer = customerRepository.findAll().iterator().next();
        testProduct = productRepository.findAll().iterator().next();
//        testOrder = orderRepository.findAll().iterator().next();
//        System.out.println(testOrder);
    }

}
