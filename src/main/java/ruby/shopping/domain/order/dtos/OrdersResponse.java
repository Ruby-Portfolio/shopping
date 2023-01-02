package ruby.shopping.domain.order.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import ruby.shopping.domain.order.Order;
import ruby.shopping.domain.orderProduct.OrderProduct;
import ruby.shopping.domain.product.Product;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrdersResponse {

    @Schema(description = "주문 목록")
    private final List<OrderDto> orders;

    public OrdersResponse(List<Order> orders) {
        this.orders = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class OrderDto {
        @Schema(description = "주문 아이디")
        private Long orderId;
        @Schema(description = "주문 날짜")
        private String createAt;
        @Schema(description = "주문 상품 목록")
        private List<OrderProductDto> orderProducts;
        @Schema(description = "총 결제 금액")
        private Integer totalPrice;

        private OrderDto(Order order) {
            this.orderId = order.getId();
            this.createAt = order.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.orderProducts = order.getOrderProducts().stream()
                    .map(OrderProductDto::new)
                    .collect(Collectors.toList());
            this.totalPrice = orderProducts.stream()
                    .mapToInt(orderProduct -> orderProduct.count * orderProduct.price)
                    .sum();
        }
    }

    @Getter
    @Setter
    public static class OrderProductDto {
        @Schema(description = "주문 상품 아이디")
        private Long orderProductId;
        @Schema(description = "주문 상품 개수")
        private Integer count;
        @Schema(description = "상품 아이디")
        private Long productId;
        @Schema(description = "상품명")
        private String productName;
        @Schema(description = "상품 가격")
        private Integer price;

        private OrderProductDto(OrderProduct orderProduct) {
            this.orderProductId = orderProduct.getId();
            this.count = orderProduct.getCount();

            Product product = orderProduct.getProduct();
            this.productId = product.getId();
            this.productName = product.getName();
            this.price = product.getPrice();
        }
    }
}
