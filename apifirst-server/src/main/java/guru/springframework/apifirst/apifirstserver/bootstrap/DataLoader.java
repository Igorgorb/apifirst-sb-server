package guru.springframework.apifirst.apifirstserver.bootstrap;

import guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import guru.springframework.apifirst.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        CreateAndSaveCustomers();
        CreateAndSaveProducts();
    }

    private void CreateAndSaveProducts() {
        Dimensions d1 = Dimensions.builder()
                .widht(100)
                .height(100)
                .lenght(100)
                .build();
        Dimensions d2 = Dimensions.builder()
                .widht(200)
                .height(300)
                .lenght(500)
                .build();

        Category c1 = Category.builder()
                .id(UUID.randomUUID())
                .category("Electricity")
                .description("Product of electricity")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();
        Category c2 = Category.builder()
                .id(UUID.randomUUID())
                .category("Tech")
                .description("Product of technics")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();
        Category c3 = Category.builder()
                .id(UUID.randomUUID())
                .category("Mechanics")
                .description("Product of mechanics")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();

        Image i1 = Image.builder()
                .id(UUID.randomUUID())
                .url("www.google.com/image1.jpg")
                .altText("image 1")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();
        Image i2 = Image.builder()
                .id(UUID.randomUUID())
                .url("www.google.com/image2.jpg")
                .altText("image 2")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();
        Image i3 = Image.builder()
                .id(UUID.randomUUID())
                .url("www.google.com/image3.jpg")
                .altText("image 3")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();
        Image i4 = Image.builder()
                .id(UUID.randomUUID())
                .url("www.google.com/image4.jpg")
                .altText("image 4")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();
        Image i5 = Image.builder()
                .id(UUID.randomUUID())
                .url("www.google.com/image5.jpg")
                .altText("image 5")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();

        Product p1 = Product.builder()
                .id(UUID.randomUUID())
                .name("Electricity")
                .description("Product of mechanics")
                .dimensions(d1)
                .categories(List.of(c1,c2))
                .images(List.of(i1,i2))
                .price("100")
                .cost("200")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();
        Product p2 = Product.builder()
                .id(UUID.randomUUID())
                .name("Fan")
                .description("Product of tech")
                .dimensions(d2)
                .categories(List.of(c3,c2))
                .images(List.of(i3,i4,i5))
                .price("9999")
                .cost("9999")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();

        productRepository.save(p1);
        productRepository.save(p2);
    }

    private void CreateAndSaveCustomers() {
        Address address1 = Address.builder()
                .addressLine1("1234 W Some Street")
                .city("Some City")
                .state("FL")
                .zip("33701")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();

        Customer customer1 = Customer.builder()
                .name(Name.builder()
                        .firstName("John")
                        .lastName("Thompson")
                        .build())
                .billToAddress(address1)
                .shipToAddress(address1)
                .email("john@springframework.guru")
                .phone("800-555-1212")
                .paymentMethods(List.of(PaymentMethod.builder()
                        .displayName("My Card")
                        .cardNumber(12341234)
                        .expiryMonth(12)
                        .expiryYear(26)
                        .cvv(123)
                        .dateCreated(OffsetDateTime.now())
                        .dateUpdated(OffsetDateTime.now())
                        .build()))
                .build();

        Address address2 = Address.builder()
                .addressLine1("1234 W Some Street")
                .city("Some City")
                .state("FL")
                .zip("33701")
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();

        Customer customer2 = Customer.builder()
                .name(Name.builder()
                        .firstName("Jim")
                        .lastName("Smith")
                        .build())
                .billToAddress(address2)
                .shipToAddress(address2)
                .email("jim@springframework.guru")
                .phone("800-555-1212")
                .paymentMethods(List.of(PaymentMethod.builder()
                        .displayName("My Other Card")
                        .cardNumber(1234888)
                        .expiryMonth(12)
                        .expiryYear(26)
                        .cvv(456)
                        .dateCreated(OffsetDateTime.now())
                        .dateUpdated(OffsetDateTime.now())
                        .build()))
                .build();

        customerRepository.save(customer1);
        customerRepository.save(customer2);
    }
}