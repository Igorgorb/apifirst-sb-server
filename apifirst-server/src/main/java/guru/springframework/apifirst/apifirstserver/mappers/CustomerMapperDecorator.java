package guru.springframework.apifirst.apifirstserver.mappers;

import guru.springframework.apifirst.apifirstserver.domain.Customer;
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
            customerPatchDto.getPaymentMethods().forEach(paymentMethodDto -> {
                customer.getPaymentMethods().stream().filter(pm -> pm.getId().equals(paymentMethodDto.getId()))
                        .findFirst().ifPresent(pm -> {
                    paymentMethodMapper.patchPaymentMethod(paymentMethodDto, pm);
                });
            });
        }

    }

    @Override
    public CustomerPatchDto customerToPatchDto(Customer customer) {
        if (customer != null) {
            if (customer.getPaymentMethods() != null) {
                List<CustomerPaymentMethodPatchDto> paymentMethodPatchDtos = new ArrayList<>();
                customer.getPaymentMethods().forEach(paymentMethod -> {
                    paymentMethodPatchDtos.add(paymentMethodMapper.paymentMethodToCustomerPaymentMethodPatchDto(paymentMethod));
                });
                CustomerPatchDto result = delegate.customerToPatchDto(customer);
                result.setPaymentMethods(paymentMethodPatchDtos);
                return result;
            }
        }
        return null;
    }
}
