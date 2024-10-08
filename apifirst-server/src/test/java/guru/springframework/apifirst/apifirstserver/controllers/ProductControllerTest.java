package guru.springframework.apifirst.apifirstserver.controllers;

import guru.springframework.apifirst.apifirstserver.config.OpenApiValidationConfig;
import guru.springframework.apifirst.apifirstserver.domain.Product;
import guru.springframework.apifirst.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(OpenApiValidationConfig.class)
public class ProductControllerTest extends BaseTest {

    @DisplayName("Test delete Product Not Found")
    @Test
    void testDeleteProductNotFound() throws Exception {
        mockMvc.perform(delete(ProductController.BASE_URL + "/{productId}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @DisplayName("Test delete Product")
    @Test
    void testDeleteProduct() throws Exception {
        ProductCreateDto productDto = buildProductCreateDto();
        Product product = productRepository.save(productMapper.dtoToProduct(productDto));

        mockMvc.perform(delete(ProductController.BASE_URL + "/{productId}", product.getId()))
                .andExpect(status().isNoContent());

        assert productRepository.findById(product.getId()).isEmpty();
    }

    @Transactional
    @Test
    void testPatchProduct() throws Exception {
        Product product = productRepository.findAll().iterator().next();

        ProductPatchDto productPatchDto = productMapper.productToPatchDto(product);

        productPatchDto.setDescription("Patched Description");

        mockMvc.perform(patch(ProductController.BASE_URL + "/{productId}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productPatchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", equalTo("Patched Description")));
    }

    @Transactional
    @Test
    void testUpdateProduct() throws Exception {
        Product product = productRepository.findAll().iterator().next();

        ProductUpdateDto productUpdateDto = productMapper.productToUpdateDto(product);

        productUpdateDto.setDescription("Updated Description");

        mockMvc.perform(put(ProductController.BASE_URL + "/{productId}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", equalTo("Updated Description")));
    }

    @DisplayName("Test Get by Id Product")
    @Test
    public void testGetProductById() throws Exception {
        mockMvc.perform(get(ProductController.BASE_URL + "/{productId}", testProduct.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testProduct.getId().toString()));
    }

    @DisplayName("Test List Products")
    @Test
    public void testListProducts() throws Exception {
        mockMvc.perform(get(ProductController.BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @DisplayName("Test create product")
    @Test
    public void testCreateProduct() throws Exception {
        ProductCreateDto newProduct = buildProductCreateDto();

        mockMvc.perform(post(ProductController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    private ProductCreateDto buildProductCreateDto() {
        return ProductCreateDto.builder()
                .description("New Product")
                .cost("5.00")
                .price("8.95")
                .categories(Arrays.asList("NEW_CATRGORY"))
                .images(Arrays.asList(ImageDto.builder()
                        .url("http://example.com/image.jpg")
                        .altText("Image Alt Text")
                        .build()))
                .dimensions(DimensionsDto.builder()
                        .length(10)
                        .width(10)
                        .height(10)
                        .build())
                .build();
    }
}
