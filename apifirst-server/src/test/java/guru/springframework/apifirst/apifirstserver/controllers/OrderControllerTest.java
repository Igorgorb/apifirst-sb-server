package guru.springframework.apifirst.apifirstserver.controllers;

import guru.springframework.apifirst.apifirstserver.domain.Order;
import guru.springframework.apifirst.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class OrderControllerTest extends BaseTest {

    @DisplayName("Test Delete Order")
    @Test
    @Transactional
    void deleteOrder() throws Exception {
        OrderCreateDto orderCreate = buildOrderCreateDto();
        Order order = orderRepository.save(orderMapper.dtoToOrder(orderCreate));

        mockMvc.perform(delete(OrderController.BASE_URL + "/{orderId}", order.getId()))
                .andExpect(status().isNoContent());

        assert orderRepository.findById(order.getId()).isEmpty();
    }

    @DisplayName("Patch Order")
    @Transactional
    @Test
    void patchOrder() throws Exception {
        Order order = orderRepository.findAll().get(0);


        OrderPatchDto orderPatchDto = OrderPatchDto.builder()
                .orderLines(Collections.singletonList(OrderLinePatchDto.builder()
                        .id(order.getOrderLines().get(0).getId())
                        .orderQuantity(222)
                        .build()))
                .build();

        mockMvc.perform(patch(OrderController.BASE_URL + "/{orderId}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderPatchDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(order.getId().toString())))
                .andExpect(jsonPath("$.orderLines[0].orderQuantity", equalTo(222)));

    }

    @DisplayName("Update Order")
    @Transactional
    @Test
    void updateOrder() throws Exception {
        Order order = orderRepository.findAll().get(0);

        order.getOrderLines().get(0).setOrderQuantity(222);
        OrderUpdateDto orderUpdateDto = orderMapper.orderToUpdateDto(order);

        mockMvc.perform(put(OrderController.BASE_URL + "/{orderId}", order.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(order.getId().toString())))
                .andExpect(jsonPath("$.orderLines[0].orderQuantity", equalTo(222)));

    }

    @DisplayName("Create New Order")
    @Test
    @Transactional
    void createNewOrder() throws Exception {
        OrderCreateDto orderCreate = buildOrderCreateDto();

        System.out.println(objectMapper.writeValueAsString(orderCreate));

        mockMvc.perform(post(OrderController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderCreate)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    private OrderCreateDto buildOrderCreateDto() {
        return OrderCreateDto.builder()
                .customerId(testCustomer.getId())
                .selectPaymentMethodId(testCustomer.getPaymentMethods().get(0).getId())
                .orderLines(Collections.singletonList(OrderLineCreateDto.builder()
                        .productId(testProduct.getId())
                        .orderQuantity(1)
                        .build()))
                .build();
    }

    @DisplayName("Test Get List Order")
    @Test
    void getOrders() throws Exception {
        mockMvc.perform(get(OrderController.BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @DisplayName("Test Get by Id Order")
    @Test
    void getOrder() throws Exception {
        mockMvc.perform(get(OrderController.BASE_URL + "/{orderId}", testOrder.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testOrder.getId().toString()));
    }
}