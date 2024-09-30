package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.apifirstserver.domain.Product;
import guru.springframework.apifirst.apifirstserver.mappers.ProductMapper;
import guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import guru.springframework.apifirst.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    public ProductDto patchProduct(UUID productId, ProductPatchDto productPatchDto) {
        Product existingProduct = productRepository.findById(productId).orElseThrow();
        productMapper.patchProduct(productPatchDto, existingProduct);

        return productMapper.productToDto(productRepository.save(existingProduct));
    }

    @Override
    public ProductDto updateProduct(UUID productId, ProductUpdateDto product) {
        Product existingProduct = productRepository.findById(productId).orElseThrow();
        productMapper.updateProduct(product, existingProduct);

        return productMapper.productToDto(productRepository.save(existingProduct));
    }

    @Override
    public List<ProductDto> findAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .map(productMapper::productToDto)
                .peek(System.out::println)
                .toList();
    }

    @Override
    public ProductDto findById(UUID id) {
        return productMapper.productToDto(productRepository.findById(id).orElseThrow());

    }

    @Override
    public ProductDto saveNewProduct(ProductCreateDto product) {
        return productMapper.productToDto(productRepository.save(productMapper.dtoToProduct(product)));
    }
}
