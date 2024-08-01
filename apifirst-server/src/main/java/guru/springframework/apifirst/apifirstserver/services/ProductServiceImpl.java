package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import guru.springframework.apifirst.model.ProductDto;
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
    public List<ProductDto> findAll() {
//        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
//                .toList();
        return null;
    }

    @Override
    public ProductDto findById(UUID id) {
//        return productRepository.findById(id).orElseThrow();
        return null;
    }

    @Override
    public ProductDto saveNewProduct(ProductDto product) {
//        return productRepository.save(product);
        return null;
    }
}
