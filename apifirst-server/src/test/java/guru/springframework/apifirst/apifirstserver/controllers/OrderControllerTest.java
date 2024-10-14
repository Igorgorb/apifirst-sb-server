package guru.springframework.apifirst.apifirstserver.controllers;

import guru.springframework.apifirst.apifirstserver.domain.Order;
import guru.springframework.apifirst.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static guru.springframework.apifirst.apifirstserver.config.OpenApiValidationConfig.OA3_SPEC;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class OrderControllerTest extends BaseTest {

    @DisplayName("Test Delete Order Not Found")
    @Test
    @Transactional
    void deleteOrderNotFound() throws Exception {
        mockMvc.perform(delete(OrderController.BASE_URL + "/{orderId}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(openApi().isValid(OA3_SPEC));
    }

    @DisplayName("Test Delete Order")
    @Test
    @Transactional
    void deleteOrder() throws Exception {
        OrderCreateDto orderCreate = buildOrderCreateDto();
        Order order = orderRepository.save(orderMapper.dtoToOrder(orderCreate));

        mockMvc.perform(delete(OrderController.BASE_URL + "/{orderId}", order.getId()))
                .andExpect(status().isNoContent())
                .andExpect(openApi().isValid(OA3_SPEC));

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
                .andExpect(jsonPath("$.orderLines[0].orderQuantity", equalTo(222)))
                .andExpect(openApi().isValid(OA3_SPEC));

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
                .andExpect(jsonPath("$.orderLines[0].orderQuantity", equalTo(222)))
                .andExpect(openApi().isValid(OA3_SPEC));

    }


    @DisplayName("Update Order Not Found")
    @Transactional
    @Test
    void updateOrderNotFound() throws Exception {
        Order order = orderRepository.findAll().get(0);
        OrderUpdateDto orderUpdateDto = orderMapper.orderToUpdateDto(order);

        mockMvc.perform(put(OrderController.BASE_URL + "/{orderId}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(openApi().isValid(OA3_SPEC));
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
                .andExpect(header().exists("Location"))
                .andExpect(openApi().isValid(OA3_SPEC));
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
                .andExpect(jsonPath("$.length()", greaterThan(0)))
                .andExpect(openApi().isValid(OA3_SPEC));
    }

    @DisplayName("Test Get by Id Order")
    @Test
    void getOrder() throws Exception {
        mockMvc.perform(get(OrderController.BASE_URL + "/{orderId}", testOrder.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testOrder.getId().toString()))
                .andExpect(openApi().isValid(OA3_SPEC));
    }

    @DisplayName("Test Get by Id Order Not Found")
    @Test
    void getOrderNotFound() throws Exception {
        mockMvc.perform(get(OrderController.BASE_URL + "/{orderId}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(openApi().isValid(OA3_SPEC));
    }
}