package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.model.OrderDto;
import guru.springframework.apifirst.model.OrderCreateDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> findAll();

    OrderDto findById(UUID orderId);

    OrderDto saveNewOrder(OrderCreateDto orderCreate);
}
