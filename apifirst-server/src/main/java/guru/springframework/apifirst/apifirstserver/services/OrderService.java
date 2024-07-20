package guru.springframework.apifirst.apifirstserver.services;

import guru.springframework.apifirst.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<Order> findAll();

    Order findById(UUID orderId);
}
