package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.model.ProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ProductDto> findAll();

    ProductDto findById(UUID id);

    ProductDto saveNewProduct(ProductDto product);
}
