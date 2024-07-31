package guru.springframework.apifirst.apifirstserver.controllers;

import guru.springframework.apifirst.model.AddressDto;
import guru.springframework.apifirst.model.CustomerDto;
import guru.springframework.apifirst.model.NameDto;
import guru.springframework.apifirst.model.PaymentMethodDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class CustomerControllerTest extends BaseTest {

    @DisplayName("Test Get by Id Customer")
    @Test
    public void testGetCustomerById() throws Exception {
        mockMvc.perform(get(CustomerController.BASE_URL + "/{customerId}", testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCustomer.getId().toString()));

    }

    @DisplayName("Test List Customers")
    @Test
    public void testListCustomers() throws Exception {
        mockMvc.perform(get(CustomerController.BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThan(0)));
    }

    @DisplayName("Test Create Customer")
    @Test
    public void testCreateCustomer() throws Exception {
        CustomerDto customer = CustomerDto.builder()
                .name(NameDto.builder()
                        .firstName("Newble")
                        .lastName("Brown")
                        .build())
                .email("newble@brown.com")
                .phone("0123456")
                .billToAddress(AddressDto.builder()
                        .city("New York")
                        .state("NY")
                        .zip("12345")
                        .addressLine1("address line 1")
                        .build())
                .shipToAddress(AddressDto.builder()
                        .city("New York")
                        .state("NY")
                        .zip("12345")
                        .addressLine1("address line 1")
                        .build())
                .paymentMethods(Arrays.asList(PaymentMethodDto.builder()
                        .displayName("Test payment method")
                        .cardNumber(123456789)
                        .cvv(123)
                        .expiryMonth(1)
                        .expiryYear(25)
                        .build()))
                .build();

        mockMvc.perform(post(CustomerController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}
