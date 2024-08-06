package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.apifirstserver.mappers.ProductMapper;
import guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import guru.springframework.apifirst.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

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
    public ProductDto saveNewProduct(ProductCreateDto product) {
        return productMapper.productToDto(productRepository.save(productMapper.dtoToProduct(product)));
//        return getProductDto();
    }

    private ProductDto getProductDto() {
        DimensionsDto d2 = DimensionsDto.builder()
                .width(200)
                .height(300)
                .length(500)
                .build();

        CategoryDto c1 = CategoryDto.builder()
                .id(UUID.randomUUID())
                .category("Electricity")
                .description("Product of electricity")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();
        CategoryDto c2 = CategoryDto.builder()
                .id(UUID.randomUUID())
                .category("Tech")
                .description("Product of technics")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();

        ImageDto i1 = ImageDto.builder()
                .id(UUID.randomUUID())
                .url("www.google.com/image1.jpg")
                .altText("image 1")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();
        ImageDto i2 = ImageDto.builder()
                .id(UUID.randomUUID())
                .url("www.google.com/image2.jpg")
                .altText("image 2")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();

        return ProductDto.builder()
                .id(UUID.randomUUID())
                .name("Electricity")
                .description("Product of mechanics")
                .dimensions(d2)
                .categories(List.of(c1, c2))
                .images(List.of(i1, i2))
                .price("100")
                .cost("200")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();
    }

}
