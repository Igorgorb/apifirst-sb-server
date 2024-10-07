package guru.springframework.apifirst.apifirstserver.mappers;

import guru.springframework.apifirst.apifirstserver.domain.Customer;
import guru.springframework.apifirst.model.CustomerDto;
import guru.springframework.apifirst.model.CustomerPatchDto;
import guru.springframework.apifirst.model.CustomerPaymentMethodPatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomerMapperDecorator implements CustomerMapper {

    @Autowired
    @Qualifier("delegate")
    private CustomerMapper delegate;

    @Autowired
    private PaymentMethodMapper paymentMethodMapper;

    @Override
    public void patchCustomer(CustomerPatchDto customerPatchDto, Customer customer) {
        delegate.patchCustomer(customerPatchDto, customer);

        if (customerPatchDto.getPaymentMethods() != null) {
            customerPatchDto.getPaymentMethods().forEach(paymentMethodPatchDto -> {
                customer.getPaymentMethods().stream().filter(pm -> pm.getId().equals(paymentMethodPatchDto.getId()))
                        .findFirst().ifPresent(pm -> {
                    paymentMethodMapper.updatePaymentMethod(paymentMethodPatchDto, pm);
                });
            });
        }

    }

    @Override
    public CustomerPatchDto customerToPatchDto(Customer customer) {
        return delegate.customerToPatchDto(customer);
    }

    @Override
    public CustomerDto customerToDto(Customer customer) {
        return delegate.customerToDto(customer);
    }

    @Override
    public Customer dtoToCustomer(CustomerDto customerDto) {
        return delegate.dtoToCustomer(customerDto);
    }

    @Override
    public void updateCustomer(CustomerDto customerDto, Customer customer) {
        delegate.updateCustomer(customerDto, customer);
    }
}
