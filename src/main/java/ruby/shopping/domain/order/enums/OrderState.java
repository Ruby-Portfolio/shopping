package ruby.shopping.domain.order.enums;

import lombok.Getter;

@Getter
public enum OrderState {
    PAYMENT_WAITING("결제 대기"),
    PAYMENT_COMPLETE("결제 완료"),
    SHIPPING("배송중"),
    SHIPPING_COMPLETE("배송 완료"),
    CANCEL("주문 취소");

    private final String value;

    OrderState(String value) {
        this.value = value;
    }
}
