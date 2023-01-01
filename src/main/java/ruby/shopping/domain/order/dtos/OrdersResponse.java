package ruby.shopping.domain.order.dtos;

import lombok.Getter;
import lombok.Setter;
import ruby.shopping.domain.order.Order;
import ruby.shopping.domain.orderProduct.OrderProduct;
import ruby.shopping.domain.product.Product;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrdersResponse {

    private final List<OrderDto> orders;

    public OrdersResponse(List<Order> orders) {
        this.orders = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class OrderDto {
        private Long productId;
        private String createAt;
        private List<OrderProductDto> orderProducts;

        private OrderDto(Order order) {
            this.productId = order.getId();
            this.createAt = order.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.orderProducts = order.getOrderProducts().stream()
                    .map(OrderProductDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @Setter
    public static class OrderProductDto {

        private Long orderProductId;
        private Integer count;
        private Long productId;
        private String productName;

        private OrderProductDto(OrderProduct orderProduct) {
            this.orderProductId = orderProduct.getId();
            this.count = orderProduct.getCount();

            Product product = orderProduct.getProduct();
            this.productId = product.getId();
            this.productName = product.getName();
        }
    }
}
