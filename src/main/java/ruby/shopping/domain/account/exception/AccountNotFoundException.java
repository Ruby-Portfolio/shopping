package ruby.shopping.domain.account.exception;

import org.springframework.http.HttpStatus;
import ruby.shopping.common.exception.CustomRuntimeException;

public class AccountNotFoundException extends CustomRuntimeException {

    public static final String MESSAGE = "계정을 찾을 수 없습니다.";

    public AccountNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
