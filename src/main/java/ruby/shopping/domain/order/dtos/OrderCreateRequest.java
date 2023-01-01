package ruby.shopping.domain.order.dtos;

import lombok.Getter;
import lombok.Setter;
import ruby.shopping.common.valid.OrderProductPattern;
import ruby.shopping.common.valid.OrderStatePattern;

import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {

    @OrderStatePattern
    private String orderState;
    @OrderProductPattern
    private List<OrderProductDto> orderProducts;

    @Getter
    @Setter
    public static class OrderProductDto {
        private Long productId;
        private Integer count;
    }
}
