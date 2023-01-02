package ruby.shopping.common.valid.validator;

import ruby.shopping.common.valid.OrderProductPattern;
import ruby.shopping.domain.order.dtos.OrderCreateRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class OrderProductValidator implements ConstraintValidator<OrderProductPattern, List<OrderCreateRequest.OrderProductDto>> {

    @Override
    public boolean isValid(List<OrderCreateRequest.OrderProductDto> value, ConstraintValidatorContext context) {
        return value.stream().allMatch(this::isOrderProduct);
    }

    private boolean isOrderProduct(OrderCreateRequest.OrderProductDto orderProductDto) {
        return isProductId(orderProductDto.getProductId()) && isCount(orderProductDto.getCount());
    }

    private boolean isProductId(Long productId) {
        return productId != null && productId > 0;
    }

    private boolean isCount(Integer count) {
        return count != null && count > 0;
    }
}
