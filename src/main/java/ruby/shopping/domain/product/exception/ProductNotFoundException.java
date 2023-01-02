package ruby.shopping.domain.product.exception;

import org.springframework.http.HttpStatus;
import ruby.shopping.common.exception.CustomRuntimeException;

public class ProductNotFoundException extends CustomRuntimeException {

    public static final String MESSAGE = "상품 정보를 찾을 수 없습니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
