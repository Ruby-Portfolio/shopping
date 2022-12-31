package ruby.shopping.domain.seller.exception;

import org.springframework.http.HttpStatus;
import ruby.shopping.common.exception.CustomRuntimeException;

public class SellerNotFoundException extends CustomRuntimeException {

    public static final String MESSAGE = "셀러 정보를 찾을 수 없습니다.";

    public SellerNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
