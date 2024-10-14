package guru.springframework.apifirst.apifirstserver.controllers;

import guru.springframework.apifirst.apifirstserver.domain.Customer;
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
public class CustomerControllerTest extends BaseTest {

    @DisplayName("Test delete Customer Conflict With Orders")
    @Test
    void testDeleteCustomerConflictWithOrders() throws Exception {
        Customer customer = customerRepository.findAll().getFirst();

        mockMvc.perform(delete(CustomerController.BASE_URL + "/{customerId}", customer.getId()))
                .andExpect(status().isConflict())
                .andExpect(openApi().isValid(OA3_SPEC));
    }

    @DisplayName("Test delete Customer Not Found")
    @Test
    void testDeleteCustomerNotFound() throws Exception {
        mockMvc.perform(delete(CustomerController.BASE_URL + "/{customerId}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(openApi().isValid(OA3_SPEC));
    }

    @DisplayName("Test delete Customer")
    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDto customerDto = buildTestCustomerDto();
        Customer customer = customerRepository.save(customerMapper.dtoToCustomer(customerDto));

        mockMvc.perform(delete(CustomerController.BASE_URL + "/{customerId}", customer.getId()))
                .andExpect(status().isNoContent())
                .andExpect(openApi().isValid(OA3_SPEC));

        assert customerRepository.findById(customer.getId()).isEmpty();
    }

    @Transactional
    @DisplayName("Test patch Customer")
    @Test
    void testPatchCustomer() throws Exception {
        Customer customer = customerRepository.findAll().getFirst();

        CustomerPatchDto customerPatchDto = customerMapper.customerToPatchDto(customer);

        customerPatchDto.setEmail("patched@email.com");

        mockMvc.perform(patch(CustomerController.BASE_URL + "/{customerId}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerPatchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo("patched@email.com")))
                .andExpect(openApi().isValid(OA3_SPEC));
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
                .andExpect(jsonPath("$.paymentMethods[0].displayName", equalTo("Test Update3")))
                .andExpect(openApi().isValid(OA3_SPEC));
    }

    @Transactional
    @DisplayName("Test update Customer Not Found")
    @Test
    void testUpdateCustomerNotFound() throws Exception {
        CustomerDto customerDto = buildTestCustomerDto();

        mockMvc.perform(put(CustomerController.BASE_URL + "/{customerId}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDto))
                ).andExpect(status().isNotFound())
                .andExpect(openApi().isValid(OA3_SPEC));
    }

    @DisplayName("Test Get by Id Customer")
    @Test
    public void testGetCustomerById() throws Exception {
        mockMvc.perform(get(CustomerController.BASE_URL + "/{customerId}", testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCustomer.getId().toString()))
                .andExpect(openApi().isValid(OA3_SPEC));

    }

    @DisplayName("Test Get by Id Customer Not Found")
    @Test
    public void testGetCustomerByIdNotFound() throws Exception {
        mockMvc.perform(get(CustomerController.BASE_URL + "/{customerId}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(openApi().isValid(OA3_SPEC));

    }

    @DisplayName("Test List Customers")
    @Test
    public void testListCustomers() throws Exception {
        mockMvc.perform(get(CustomerController.BASE_URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThan(0)))
                .andExpect(openApi().isValid(OA3_SPEC));
    }

    @DisplayName("Test Create Customer")
    @Test
    public void testCreateCustomer() throws Exception {
        CustomerDto customer = buildTestCustomerDto();

        mockMvc.perform(post(CustomerController.BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(openApi().isValid(OA3_SPEC));
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
