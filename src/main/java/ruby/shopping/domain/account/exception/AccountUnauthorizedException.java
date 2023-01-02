package ruby.shopping.domain.account.exception;

import org.springframework.http.HttpStatus;
import ruby.shopping.common.exception.CustomRuntimeException;

public class AccountUnauthorizedException extends CustomRuntimeException {

    public static final String MESSAGE = "접근 권한이 없습니다.";

    public AccountUnauthorizedException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
