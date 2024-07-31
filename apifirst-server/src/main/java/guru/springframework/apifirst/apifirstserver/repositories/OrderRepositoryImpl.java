package guru.springframework.apifirst.apifirstserver.repositories;

import guru.springframework.apifirst.model.*;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class OrderRepositoryImpl implements OrderRepository {


    private final Map<UUID, OrderDto> entityMap = new HashMap<>();

    @Override
    public <S extends OrderDto> S save(S entity) {
        UUID id = UUID.randomUUID();

        OrderDto.OrderDtoBuilder builder1 = OrderDto.builder();

        builder1.id(id)
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now());
        if (entity.getCustomer() != null) {
            OrderCustomerDto orderCustomer = getOrderCustomer(entity);
            System.out.println("orderCustomer: " + orderCustomer);
            builder1.customer(orderCustomer);
        }
        builder1.orderStatus(entity.getOrderStatus())
                .shipmentInfo(entity.getShipmentInfo());
        if (entity.getOrderLines() != null) {
            builder1.orderLines(entity.getOrderLines().stream()
                    .map(orderLine -> OrderLineDto.builder()
                            .id(UUID.randomUUID())
                            .product(OrderProductDto.builder()
                                    .id(orderLine.getProduct().getId())
                                    .description(orderLine.getProduct().getDescription())
                                    .price(orderLine.getProduct().getPrice())
                                    .build())
                            .orderQuantity(orderLine.getOrderQuantity())
                            .shipQuantity(orderLine.getShipQuantity())
                            .dateCreated(OffsetDateTime.now())
                            .dateUpdated(OffsetDateTime.now())
                            .build())
                    .toList());
        }
        OrderDto order = builder1.build();
        entityMap.put(id, order);
        return (S) order;
    }

    private static <S extends OrderDto> OrderCustomerDto getOrderCustomer(S entity) {
        OrderCustomerDto orderCustomer = entity.getCustomer();
        OrderCustomerDto.OrderCustomerDtoBuilder ocBuilder = OrderCustomerDto.builder();
        ocBuilder.id(orderCustomer.getId())
                .phone(orderCustomer.getPhone())
                .email(orderCustomer.getEmail());

        if (orderCustomer.getName() != null) {
            ocBuilder.name(NameDto.builder()
                    .firstName(orderCustomer.getName().getFirstName())
                    .lastName(orderCustomer.getName().getLastName())
                    .prefix(orderCustomer.getName().getPrefix())
                    .suffix(orderCustomer.getName().getSuffix())
                    .build());
        }

        if (orderCustomer.getBillToAddress() != null) {
            ocBuilder.billToAddress(AddressDto.builder()
                    .id(UUID.randomUUID())
                    .addressLine1(orderCustomer.getBillToAddress().getAddressLine1())
                    .addressLine2(orderCustomer.getBillToAddress().getAddressLine2())
                    .city(orderCustomer.getBillToAddress().getCity())
                    .state(orderCustomer.getBillToAddress().getState())
                    .zip(orderCustomer.getBillToAddress().getZip())
                    .dateCreated(OffsetDateTime.now())
                    .dateUpdated(OffsetDateTime.now())
                    .build());
        }
        if (orderCustomer.getShipToAddress() != null) {
            ocBuilder.shipToAddress(AddressDto.builder()
                    .id(UUID.randomUUID())
                    .addressLine1(orderCustomer.getShipToAddress().getAddressLine1())
                    .addressLine2(orderCustomer.getShipToAddress().getAddressLine2())
                    .city(orderCustomer.getShipToAddress().getCity())
                    .state(orderCustomer.getShipToAddress().getState())
                    .zip(orderCustomer.getShipToAddress().getZip())
                    .dateCreated(OffsetDateTime.now())
                    .dateUpdated(OffsetDateTime.now())
                    .build());
        }
        if (orderCustomer.getSelectedPaymentMethod() != null) {
            ocBuilder.selectedPaymentMethod(PaymentMethodDto.builder()
                    .id(UUID.randomUUID())
                    .displayName(orderCustomer.getSelectedPaymentMethod().getDisplayName())
                    .cardNumber(orderCustomer.getSelectedPaymentMethod().getCardNumber())
                    .expiryMonth(orderCustomer.getSelectedPaymentMethod().getExpiryMonth())
                    .expiryYear(orderCustomer.getSelectedPaymentMethod().getExpiryYear())
                    .cvv(orderCustomer.getSelectedPaymentMethod().getCvv())
                    .dateCreated(OffsetDateTime.now())
                    .dateUpdated(OffsetDateTime.now())
                    .build());
        }
        return ocBuilder.build();
    }

    @Override
    public <S extends OrderDto> Iterable<S> saveAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDto> findById(UUID uuid) {
        return Optional.of(entityMap.get(uuid));
    }

    @Override
    public boolean existsById(UUID uuid) {
        return entityMap.get(uuid) != null;
    }

    @Override
    public Iterable<OrderDto> findAll() {
        return entityMap.values();
    }

    @Override
    public Iterable<OrderDto> findAllById(Iterable<UUID> uuids) {
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
    public void delete(OrderDto entity) {
        entityMap.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        uuids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends OrderDto> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        entityMap.clear();
    }
}
