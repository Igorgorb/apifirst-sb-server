package guru.springframework.apifirst.apifirstserver.repositories;

import guru.springframework.apifirst.model.CategoryDto;
import guru.springframework.apifirst.model.DimensionsDto;
import guru.springframework.apifirst.model.ImageDto;
import guru.springframework.apifirst.model.ProductDto;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final Map<UUID, ProductDto> entityMap = new HashMap<>();

    @Override
    public <S extends ProductDto> S save(S entity) {
        UUID id = UUID.randomUUID();

        ProductDto.ProductDtoBuilder builder1 = ProductDto.builder();

        builder1.id(id);
        if (entity.getDimensions() != null) {
            builder1.dimensions(DimensionsDto.builder()
                    .width(entity.getDimensions().getWidth())
                    .height(entity.getDimensions().getHeight())
                    .length(entity.getDimensions().getLength())
                    .build()
            );
        }
        if (entity.getCategories() != null) {
            builder1.categories(entity.getCategories()
                    .stream()
                    .map(category -> CategoryDto.builder()
                            .id(UUID.randomUUID())
                            .category(category.getCategory())
                            .description(category.getDescription())
                            .dateCreated(category.getDateCreated())
                            .dateUpdated(category.getDateUpdated())
                            .build()
                    ).collect(Collectors.toList())
            );
        }
        if (entity.getImages() != null) {
            builder1.images(entity.getImages().stream()
                    .map(image -> ImageDto.builder()
                            .id(UUID.randomUUID())
                            .url(image.getUrl())
                            .altText(image.getAltText())
                            .dateCreated(image.getDateCreated())
                            .dateUpdated(image.getDateUpdated())
                            .build())
                    .collect(Collectors.toList())
            );
        }
        ProductDto product = builder1.name(entity.getName())
                .cost(entity.getCost())
                .price(entity.getPrice())
                .description(entity.getDescription())
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();

        entityMap.put(id, product);

        return (S) product;
    }

    @Override
    public <S extends ProductDto> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<ProductDto> findById(UUID uuid) {
        return Optional.of(entityMap.get(uuid));
    }

    @Override
    public boolean existsById(UUID uuid) {
        return entityMap.get(uuid) != null;
    }

    @Override
    public Iterable<ProductDto> findAll() {
        return entityMap.values();
    }

    @Override
    public Iterable<ProductDto> findAllById(Iterable<UUID> uuids) {
        return StreamSupport.stream(uuids.spliterator(), false)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return entityMap.size();
    }

    @Override
    public void deleteById(UUID uuid) {
        entityMap.remove(uuid);
    }

    @Override
    public void delete(ProductDto entity) {
        entityMap.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        uuids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends ProductDto> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        entityMap.clear();
    }
}
