package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import guru.springframework.apifirst.apifirstserver.repositories.OrderRepository;
import guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import guru.springframework.apifirst.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Order> findAll() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .toList();
    }

    @Override
    public Order findById(UUID orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    @Override
    public Order saveNewOrder(OrderCreate orderCreate) {
        Customer orderCustomer = customerRepository.findById(orderCreate.getCustomerId()).orElseThrow();

        Order.OrderBuilder builder = Order.builder()
                .customer(OrderCustomer.builder()
                        .id(orderCustomer.getId())
                        .name(orderCustomer.getName())
                        .billToAddress(orderCustomer.getBillToAddress())
                        .shipToAddress(orderCustomer.getShipToAddress())
                        .phone(orderCustomer.getPhone())
                        .selectedPaymentMethod(orderCustomer.getPaymentMethods().stream()
                                .filter(paymentMethod -> paymentMethod.getId()
                                        .equals(orderCreate.getSelectPaymentMethodId()))
                                .findFirst().orElseThrow())
                        .build())
                .orderStatus(Order.OrderStatusEnum.NEW);

        List<OrderLine> orderLines = new ArrayList<>();

        orderCreate.getOrderLines()
                .forEach(orderLineCreate -> {
                    Product product = productRepository.findById(orderLineCreate.getProductId()).orElseThrow();

                    orderLines.add(OrderLine.builder()
                            .product(OrderProduct.builder()
                                    .id(product.getId())
                                    .description(product.getDescription())
                                    .price(product.getPrice())
                                    .build())
                            .orderQuantity(orderLineCreate.getOrderQuantity())
                            .build());
                });

        return orderRepository.save(builder.orderLines(orderLines).build());
    }
}
