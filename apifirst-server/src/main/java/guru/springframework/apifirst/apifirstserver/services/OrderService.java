package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.model.OrderDto;
import guru.springframework.apifirst.model.OrderCreateDto;
import guru.springframework.apifirst.model.OrderPatchDto;
import guru.springframework.apifirst.model.OrderUpdateDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderDto> findAll();

    OrderDto findById(UUID orderId);

    OrderDto saveNewOrder(OrderCreateDto orderCreate);


    OrderDto updateOrder(UUID orderId, OrderUpdateDto orderUpdateDto);

    OrderDto patchOrder(UUID orderId, OrderPatchDto orderPatchDto);

    void deleteOrder(UUID orderId);
}
