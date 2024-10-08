package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.model.ProductCreateDto;
import guru.springframework.apifirst.model.ProductDto;
import guru.springframework.apifirst.model.ProductPatchDto;
import guru.springframework.apifirst.model.ProductUpdateDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductDto updateProduct(UUID productId,  ProductUpdateDto product);

    List<ProductDto> findAll();

    ProductDto findById(UUID id);

    ProductDto saveNewProduct(ProductCreateDto product);

    ProductDto patchProduct(UUID productId, ProductPatchDto productPatchDto);

    void deleteProduct(UUID productId);
}
