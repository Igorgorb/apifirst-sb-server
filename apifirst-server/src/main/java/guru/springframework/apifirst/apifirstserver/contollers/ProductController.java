package guru.springframework.apifirst.apifirstserver.contollers;

import guru.springframework.apifirst.apifirstserver.services.ProductService;
import guru.springframework.apifirst.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static guru.springframework.apifirst.apifirstserver.contollers.ProductController.BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
public class ProductController {

    public static final String BASE_URL = "/v1/products";

    private final ProductService productService;
    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(productService.findById(productId));
    }
}
