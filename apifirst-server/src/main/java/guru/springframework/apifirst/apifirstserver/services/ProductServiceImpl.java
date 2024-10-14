package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.apifirstserver.domain.Product;
import guru.springframework.apifirst.apifirstserver.mappers.ProductMapper;
import guru.springframework.apifirst.apifirstserver.repositories.OrderRepository;
import guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import guru.springframework.apifirst.model.ProductCreateDto;
import guru.springframework.apifirst.model.ProductDto;
import guru.springframework.apifirst.model.ProductPatchDto;
import guru.springframework.apifirst.model.ProductUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public void deleteProduct(UUID productId) {
        productRepository.findById(productId).ifPresentOrElse(product -> {
                    if (!orderRepository.findAllByOrderLines_Product(product).isEmpty()) {
                        throw new ConflictException("Product is already in order");
                    }
                    productRepository.delete(product);
                },
                () -> {
                    throw new NotFoundException("Product not found");
                }
        );
    }

    @Override
    public ProductDto patchProduct(UUID productId, ProductPatchDto productPatchDto) {
        Product existingProduct = productRepository.findById(productId).orElseThrow();
        productMapper.patchProduct(productPatchDto, existingProduct);

        return productMapper.productToDto(productRepository.save(existingProduct));
    }

    @Override
    public ProductDto updateProduct(UUID productId, ProductUpdateDto product) {
        Product existingProduct = productRepository.findById(productId).orElseThrow(NotFoundException::new);
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
        return productMapper.productToDto(productRepository.findById(id).orElseThrow(NotFoundException::new));

    }

    @Override
    public ProductDto saveNewProduct(ProductCreateDto product) {
        return productMapper.productToDto(productRepository.save(productMapper.dtoToProduct(product)));
    }
}
