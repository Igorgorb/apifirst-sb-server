package guru.springframework.apifirst.apifirstserver.mappers;


import guru.springframework.apifirst.apifirstserver.domain.PaymentMethod;
import guru.springframework.apifirst.model.CustomerPaymentMethodPatchDto;
import guru.springframework.apifirst.model.PaymentMethodDto;
import org.mapstruct.*;

@Mapper
public interface PaymentMethodMapper {

    @Mapping(target = "dateCreated", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updatePaymentMethod(CustomerPaymentMethodPatchDto paymentMethodDto, @MappingTarget PaymentMethod paymentMethod);

    PaymentMethodDto paymentMethodToDto(PaymentMethod paymentMethod);
}
