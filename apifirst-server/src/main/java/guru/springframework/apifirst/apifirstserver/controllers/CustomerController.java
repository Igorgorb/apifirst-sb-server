package guru.springframework.apifirst.apifirstserver.controllers;

import guru.springframework.apifirst.apifirstserver.services.CustomerService;
import guru.springframework.apifirst.model.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

import static guru.springframework.apifirst.apifirstserver.controllers.CustomerController.BASE_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_URL)
public class CustomerController {

    public static final String BASE_URL
            = "/v1/customers";

    private final CustomerService customerService;

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable("customerId") UUID customerId,
                                                      @RequestBody CustomerDto customerDto) {
        CustomerDto customerDtoUpdated = customerService.updateCustomer(customerId, customerDto);
        return ResponseEntity.ok(customerDtoUpdated);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        return ResponseEntity.ok(customerService.listCustomers());
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("customerId") UUID customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @PostMapping
    public ResponseEntity<Void> createCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto savedCustomer = customerService.saveNewCustomer(customerDto);

        UriComponents uriComponents = UriComponentsBuilder.fromPath(BASE_URL + "/{customerId}")
                .buildAndExpand(savedCustomer.getId());

        return ResponseEntity.created(uriComponents.toUri()).build();

    }
}
