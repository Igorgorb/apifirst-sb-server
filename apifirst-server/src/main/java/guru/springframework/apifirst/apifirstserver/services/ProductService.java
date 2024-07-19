package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<Product> findAll();

    Product findById(UUID id);
}
