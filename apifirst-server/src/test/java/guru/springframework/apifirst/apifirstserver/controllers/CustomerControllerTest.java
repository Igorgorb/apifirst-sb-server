package guru.springframework.apifirst.apifirstserver.controllers;

import guru.springframework.apifirst.apifirstserver.domain.Customer;
import guru.springframework.apifirst.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class CustomerControllerTest extends BaseTest {

    @DisplayName("Test delete Customer")
    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDto customerDto = buildTestCustomerDto();
        Customer customer = customerRepository.save(customerMapper.dtoToCustomer(customerDto));

        mockMvc.perform(delete(CustomerController.BASE_URL + "/{customerId}", customer.getId()))
                .andExpect(status().isNoContent());

        assert customerRepository.findById(customer.getId()).isEmpty();
    }

    @Transactional
    @DisplayName("Test patch Customer")
    @Test
    void testPatchCustomer() throws Exception {
        Customer customer = customerRepository.findAll().iterator().next();

        CustomerPatchDto customerPatchDto = customerMapper.customerToPatchDto(customer);

        customerPatchDto.setEmail("patched@email.com");

        mockMvc.perform(patch(CustomerController.BASE_URL + "/{customerId}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerPatchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo("patched@email.com")));
    }

    @Transactional
    @DisplayName("Test update Customer")
    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerRepository.findAll().iterator().next();

        customer.setEmail("test_to_update@gmail.com");
        customer.getName().setFirstName("Test Update1");
        customer.getName().setLastName("Test Update2");
        customer.getPaymentMethods().get(0).setDisplayName("Test Update3");

        mockMvc.perform(put(CustomerController.BASE_URL + "/{customerId}", customer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMapper.customerToDto(customer)))
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", equalTo("test_to_update@gmail.com")))
                .andExpect(jsonPath("$.name.firstName", equalTo("Test Update1")))
                .andExpect(jsonPath("$.name.lastName", equalTo("Test Update2")))
                .andExpect(jsonPath("$.paymentMethods[0].displayName", equalTo("Test Update3")));
    }

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
        CustomerDto customer = buildTestCustomerDto();

        mockMvc.perform(post(CustomerController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    private CustomerDto buildTestCustomerDto() {
        return CustomerDto.builder()
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
                .paymentMethods(Collections.singletonList(PaymentMethodDto.builder()
                        .displayName("Test payment method")
                        .cardNumber(123456789)
                        .cvv(123)
                        .expiryMonth(1)
                        .expiryYear(25)
                        .build()))
                .build();
    }
}
