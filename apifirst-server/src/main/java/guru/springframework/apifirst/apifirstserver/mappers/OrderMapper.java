package guru.springframework.apifirst.apifirstserver.mappers;


import guru.springframework.apifirst.apifirstserver.domain.Order;
import guru.springframework.apifirst.model.OrderDto;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    Order dtoToOrder(OrderDto dto);

    OrderDto orderToDto(Order order);
}
