package ruby.shopping.domain.account.exception;

import org.springframework.http.HttpStatus;
import ruby.shopping.common.exception.CustomRuntimeException;

public class AccountExistsEmailException extends CustomRuntimeException {

    public static final String MESSAGE = "해당 이메일로 가입된 계정이 이미 존재합니다.";

    public AccountExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.CONFLICT.value();
    }
}
