package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import guru.springframework.apifirst.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public Product findById(UUID id) {
        return productRepository.findById(id).orElseThrow();
    }
}
