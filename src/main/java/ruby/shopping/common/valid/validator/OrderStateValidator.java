package ruby.shopping.common.valid.validator;

import ruby.shopping.common.valid.OrderStatePattern;
import ruby.shopping.domain.order.enums.OrderState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class OrderStateValidator implements ConstraintValidator<OrderStatePattern, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(OrderState.values())
                .anyMatch(orderState -> orderState.name().equals(value));
    }
}
