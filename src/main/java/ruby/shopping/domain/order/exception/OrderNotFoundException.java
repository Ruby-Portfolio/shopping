package ruby.shopping.domain.order.exception;

import org.springframework.http.HttpStatus;
import ruby.shopping.common.exception.CustomRuntimeException;

public class OrderNotFoundException extends CustomRuntimeException {

    public static final String MESSAGE = "주문 정보를 찾을 수 없습니다.";

    public OrderNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
