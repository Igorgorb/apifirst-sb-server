package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.model.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> listCategories();
}
