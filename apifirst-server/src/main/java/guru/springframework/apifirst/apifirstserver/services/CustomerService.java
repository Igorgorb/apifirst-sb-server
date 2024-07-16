package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.model.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> listCustomers();
}
