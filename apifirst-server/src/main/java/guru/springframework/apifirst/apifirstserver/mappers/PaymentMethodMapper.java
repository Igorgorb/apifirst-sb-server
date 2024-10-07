package guru.springframework.apifirst.apifirstserver.mappers;


import guru.springframework.apifirst.apifirstserver.domain.PaymentMethod;
import guru.springframework.apifirst.model.CustomerPaymentMethodPatchDto;
import guru.springframework.apifirst.model.PaymentMethodDto;
import org.mapstruct.*;

@Mapper
public interface PaymentMethodMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void patchPaymentMethod(CustomerPaymentMethodPatchDto paymentMethodDto, @MappingTarget PaymentMethod paymentMethod);

    CustomerPaymentMethodPatchDto paymentMethodToCustomerPaymentMethodPatchDto(PaymentMethod paymentMethod);

    PaymentMethodDto paymentMethodToDto(PaymentMethod paymentMethod);
}
