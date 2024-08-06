package guru.springframework.apifirst.apifirstserver.mappers;

import guru.springframework.apifirst.apifirstserver.domain.Category;
import guru.springframework.apifirst.apifirstserver.domain.Product;
import guru.springframework.apifirst.apifirstserver.repositories.CategoryRepository;
import guru.springframework.apifirst.model.ProductCreateDto;
import guru.springframework.apifirst.model.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductMapperDecorator implements ProductMapper {

    @Autowired
    @Qualifier("delegate")
    private ProductMapper productMapperDelegate;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Product dtoToProduct(ProductDto dto) {
        return productMapperDelegate.dtoToProduct(dto);
    }

    @Override
    public Product dtoToProduct(ProductCreateDto productCreateDto) {
        if(productCreateDto != null) {
            Product product = productMapperDelegate.dtoToProduct(productCreateDto);

            if(productCreateDto.getCategories() != null) {
                List<Category> categories = new ArrayList<>();
                productCreateDto.getCategories().forEach(
                        categoryCode ->{
                            categoryRepository.findByCategoryCode(categoryCode).ifPresent(categories::add);
                        }
                );
                product.setCategories(categories);
                return product;
            }
        }
        return null;
    }

    @Override
    public ProductDto productToDto(Product product) {
        return productMapperDelegate.productToDto(product);
    }
}
