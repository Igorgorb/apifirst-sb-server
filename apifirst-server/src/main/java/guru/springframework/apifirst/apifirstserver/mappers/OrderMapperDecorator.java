package guru.springframework.apifirst.apifirstserver.mappers;

import guru.springframework.apifirst.apifirstserver.domain.*;
import guru.springframework.apifirst.apifirstserver.repositories.CustomerRepository;
import guru.springframework.apifirst.apifirstserver.repositories.ProductRepository;
import guru.springframework.apifirst.model.OrderCreateDto;
import guru.springframework.apifirst.model.OrderDto;
import guru.springframework.apifirst.model.OrderPatchDto;
import guru.springframework.apifirst.model.OrderUpdateDto;
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
    public void patchOrder(OrderPatchDto orderPatchDto, Order order) {
        orderMapperDelegate.patchOrder(orderPatchDto, order);

        if (orderPatchDto.getCustomerId() != null) {
            Customer customer = customerRepository.findById(orderPatchDto.getCustomerId()).orElseThrow();
            order.setCustomer(customer);
        }

        if (orderPatchDto.getSelectPaymentMethodId() != null) {
            PaymentMethod selectedPaymentMethod = order.getCustomer().getPaymentMethods().stream()
                    .filter(pm -> pm.getId().equals(orderPatchDto.getSelectPaymentMethodId()))
                    .findFirst()
                    .orElseThrow();
            order.setSelectedPaymentMethod(selectedPaymentMethod);
        }

        if (orderPatchDto.getOrderLines() != null && !orderPatchDto.getOrderLines().isEmpty()) {
            orderPatchDto.getOrderLines().forEach(orderLinePatchDto -> {
                OrderLine existingOrderLine = order.getOrderLines().stream()
                        .filter(ol -> ol.getId().equals(orderLinePatchDto.getId()))
                        .findFirst()
                        .orElseThrow();

                if (orderLinePatchDto.getProductId() != null) {
                    Product product = productRepository.findById(orderLinePatchDto.getProductId()).orElseThrow();
                    existingOrderLine.setProduct(product);
                }

                if (orderLinePatchDto.getOrderQuantity() != null) {
                    existingOrderLine.setOrderQuantity(orderLinePatchDto.getOrderQuantity());
                }
            });
        }
    }

    @Override
    public void updateOrder(OrderUpdateDto orderDto, Order order) {
        orderMapperDelegate.updateOrder(orderDto, order);

        Customer orderCustomer = customerRepository.findById(orderDto.getCustomerId()).orElseThrow();

        order.setCustomer(orderCustomer);

        PaymentMethod selectedPaymentMethod = order.getCustomer().getPaymentMethods().stream()
                .filter(pm -> pm.getId().equals(orderDto.getSelectPaymentMethodId()))
                .findFirst()
                .orElseThrow();

        order.setSelectedPaymentMethod(selectedPaymentMethod);

        if (orderDto.getOrderLines() != null && !orderDto.getOrderLines().isEmpty()) {
            orderDto.getOrderLines().forEach(orderLineDto -> {
                OrderLine existingOrderLine = order.getOrderLines().stream()
                        .filter(ol -> ol.getId().equals(orderLineDto.getId()))
                        .findFirst().orElseThrow();

                Product product = productRepository.findById(orderLineDto.getProductId()).orElseThrow();

                existingOrderLine.setProduct(product);
                existingOrderLine.setOrderQuantity(orderLineDto.getOrderQuantity());
            });
        }
    }


    @Override
    public OrderUpdateDto orderToUpdateDto(Order order) {
        OrderUpdateDto orderUpdateDto = orderMapperDelegate.orderToUpdateDto(order);

        orderUpdateDto.setCustomerId(order.getCustomer().getId());
        orderUpdateDto.setSelectPaymentMethodId(order.getSelectedPaymentMethod().getId());

        orderUpdateDto.getOrderLines().forEach(orderLineDto -> {
            OrderLine orderLine = order.getOrderLines().stream()
                    .filter(ol -> ol.getId().equals(orderLineDto.getId()))
                    .findFirst()
                    .orElseThrow();
            orderLineDto.setProductId(orderLine.getProduct().getId());
        });

        return orderUpdateDto;
    }

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

        Order newOrder = builder.orderLines(orderLines).build();

        newOrder.getOrderLines().forEach(ol -> ol.setOrder(newOrder));

        return newOrder;
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
