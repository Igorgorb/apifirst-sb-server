package guru.springframework.apifirst.apifirstserver.controllers;

import guru.springframework.apifirst.apifirstserver.services.ProductService;
import guru.springframework.apifirst.model.ProductCreateDto;
import guru.springframework.apifirst.model.ProductDto;
import guru.springframework.apifirst.model.ProductPatchDto;
import guru.springframework.apifirst.model.ProductUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static guru.springframework.apifirst.apifirstserver.controllers.ProductController.BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
public class ProductController {

    public static final String BASE_URL = "/v1/products";

    private final ProductService productService;

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") UUID productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductDto> patchProduct(@PathVariable("productId") UUID productId,
                                                   @RequestBody ProductPatchDto productPatchDto) {

        final ProductDto productDto = productService.patchProduct(productId, productPatchDto);
        return ResponseEntity.ok(productDto);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("productId") UUID productId,
                                                    @RequestBody ProductUpdateDto productUpdateDto) {
        final ProductDto productDto = productService.updateProduct(productId, productUpdateDto);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(productService.findById(productId));
    }

    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductCreateDto product) {
        ProductDto savedProduct = productService.saveNewProduct(product);

        UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL + "/{productId}")
                .buildAndExpand(savedProduct.getId());

        return ResponseEntity.created(uriComponents.toUri()).build();
    }
}
