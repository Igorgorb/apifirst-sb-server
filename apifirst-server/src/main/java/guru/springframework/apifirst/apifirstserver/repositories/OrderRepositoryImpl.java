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


    private final Map<UUID, Order> entityMap = new HashMap<>();

    @Override
    public <S extends Order> S save(S entity) {
        UUID id = UUID.randomUUID();

        Order.OrderBuilder builder1 = Order.builder();

        builder1.id(id)
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now());
        if (entity.getCustomer() != null) {
            OrderCustomer orderCustomer = getOrderCustomer(entity);
            System.out.println("orderCustomer: " + orderCustomer);
            builder1.customer(orderCustomer);
        }
        builder1.orderStatus(entity.getOrderStatus())
                .shipmentInfo(entity.getShipmentInfo());
        if (entity.getOrderLines() != null) {
            builder1.orderLines(entity.getOrderLines().stream()
                    .map(orderLine -> OrderLine.builder()
                            .id(UUID.randomUUID())
                            .product(OrderProduct.builder()
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
        Order order = entityMap.put(id, builder1.build());
        return (S) order;
    }

    private static <S extends Order> OrderCustomer getOrderCustomer(S entity) {
        OrderCustomer orderCustomer = entity.getCustomer();
        OrderCustomer.OrderCustomerBuilder ocBuilder = OrderCustomer.builder();
        ocBuilder.id(orderCustomer.getId())
                .phone(orderCustomer.getPhone())
                .email(orderCustomer.getEmail());

        if (orderCustomer.getName() != null) {
            ocBuilder.name(Name.builder()
                    .firstName(orderCustomer.getName().getFirstName())
                    .lastName(orderCustomer.getName().getLastName())
                    .prefix(orderCustomer.getName().getPrefix())
                    .suffix(orderCustomer.getName().getSuffix())
                    .build());
        }

        if (orderCustomer.getBillToAddress() != null) {
            ocBuilder.billToAddress(Address.builder()
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
            ocBuilder.shipToAddress(Address.builder()
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
            ocBuilder.selectedPaymentMethod(PaymentMethod.builder()
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
    public <S extends Order> Iterable<S> saveAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Order> findById(UUID uuid) {
        return Optional.of(entityMap.get(uuid));
    }

    @Override
    public boolean existsById(UUID uuid) {
        return entityMap.get(uuid) != null;
    }

    @Override
    public Iterable<Order> findAll() {
        return entityMap.values();
    }

    @Override
    public Iterable<Order> findAllById(Iterable<UUID> uuids) {
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
    public void delete(Order entity) {
        entityMap.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        uuids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends Order> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        entityMap.clear();
    }
}
