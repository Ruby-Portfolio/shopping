package ruby.shopping.domain.order.dtos;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class OrderResponse {

    @Schema(description = "주문 아이디", example = "1")
    private final Long orderId;
    @Schema(description = "주문 날짜", example = "2023-01-10")
    private final String createAt;
    @Schema(description = "총 결제 금액", example = "40000000")
    private final Integer totalPrice;

    @QueryProjection
    public OrderResponse(Long orderId, LocalDateTime createAt, Integer totalPrice) {
        this.orderId = orderId;
        this.createAt = createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.totalPrice = totalPrice;
    }
}
