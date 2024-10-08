package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.apifirstserver.domain.Order;
import guru.springframework.apifirst.apifirstserver.mappers.OrderMapper;
import guru.springframework.apifirst.apifirstserver.repositories.OrderRepository;
import guru.springframework.apifirst.model.OrderCreateDto;
import guru.springframework.apifirst.model.OrderDto;
import guru.springframework.apifirst.model.OrderPatchDto;
import guru.springframework.apifirst.model.OrderUpdateDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> findAll() {
        return StreamSupport.stream(orderRepository.findAll().spliterator(), false)
                .map(orderMapper::orderToDto)
                .toList();
    }

    @Override
    public OrderDto findById(UUID orderId) {
        return orderMapper.orderToDto(orderRepository.findById(orderId).orElseThrow());
    }

    @Override
    public OrderDto saveNewOrder(OrderCreateDto orderCreate) {
        Order savedOrder = orderRepository.saveAndFlush(orderMapper.dtoToOrder(orderCreate));

        return orderMapper.orderToDto(savedOrder);
    }

    @Transactional
    @Override
    public OrderDto updateOrder(UUID orderId, OrderUpdateDto orderUpdateDto) {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow();

        orderMapper.updateOrder(orderUpdateDto, existingOrder);

        Order savedOrder = orderRepository.saveAndFlush(existingOrder);

        return orderMapper.orderToDto(savedOrder);
    }

    @Override
    public OrderDto patchOrder(UUID orderId, OrderPatchDto orderPatchDto) {
        Order existingOrder = orderRepository.findById(orderId).orElseThrow();

        orderMapper.patchOrder(orderPatchDto, existingOrder);

        return orderMapper.orderToDto(orderRepository.saveAndFlush(existingOrder));
    }

    @Transactional
    @Override
    public void deleteOrder(UUID orderId) {
        orderRepository.deleteById(orderId);
    }
}
