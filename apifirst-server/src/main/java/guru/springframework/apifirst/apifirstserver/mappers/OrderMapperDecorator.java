package guru.springframework.apifirst.apifirstserver.mappers;

import guru.springframework.apifirst.apifirstserver.domain.*;
import guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import guru.springframework.apifirst.model.OrderCreateDto;
import guru.springframework.apifirst.model.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

public abstract class OrderMapperDecorator implements OrderMapper {

    @Autowired
    @Qualifier("delegate")
    private OrderMapper orderMapperDelegate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Override
    public Order dtoToOrder(OrderCreateDto orderCreate) {
        Customer orderCustomer = customerRepository.findById(orderCreate.getCustomerId()).orElseThrow();

        PaymentMethod selectedPaymentMethod = orderCustomer.getPaymentMethods().stream()
                .filter(pm -> pm.getId().equals(orderCreate.getSelectPaymentMethodId()))
                .findFirst()
                .orElseThrow();

        Order.OrderBuilder builder = Order.builder()
                .customer(orderCustomer)
                .selectedPaymentMethod(selectedPaymentMethod)
                .orderStatus(OrderStatus.NEW);

        List<OrderLine> orderLines = new ArrayList<>();

        orderCreate.getOrderLines()
                .forEach(orderLineCreate -> {
                    Product orderProduct = productRepository.findById(orderLineCreate.getProductId()).orElseThrow();

                    orderLines.add(OrderLine.builder()
                            .product(orderProduct)
                            .orderQuantity(orderLineCreate.getOrderQuantity())
                            .build());
                });

        return builder.orderLines(orderLines).build();
    }

    @Override
    public Order dtoToOrder(OrderDto dto) {
        return orderMapperDelegate.dtoToOrder(dto);
    }

    @Override
    public OrderDto orderToDto(Order order) {
        OrderDto orderDto = orderMapperDelegate.orderToDto(order);
        orderDto.getCustomer()
                .selectedPaymentMethod(paymentMethodMapper.paymentMethodToDto(order.getSelectedPaymentMethod()));
        return orderDto;
    }
}
