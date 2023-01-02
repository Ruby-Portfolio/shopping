package ruby.shopping.domain.order.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ruby.shopping.common.valid.OrderProductPattern;
import ruby.shopping.common.valid.OrderStatePattern;

import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {

    @Schema(description = "주문 상태. PAYMENT_WAITING / PAYMENT_COMPLETE / SHIPPING / SHIPPING_COMPLETE", example = "PAYMENT_WAITING")
    @OrderStatePattern
    private String orderState;
    @Schema(description = "주문 상품 목록", example = "{productId : 1, count : 3}")
    @OrderProductPattern
    private List<OrderProductDto> orderProducts;

    @Getter
    @Setter
    public static class OrderProductDto {
        @Schema(description = "상품 아이디", example = "1")
        private Long productId;
        @Schema(description = "주문 상품 개수", example = "3")
        private Integer count;
    }
}
