package ruby.shopping.common.valid;

import ruby.shopping.common.valid.validator.AccountNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = AccountNumberValidator.class)
@Documented
public @interface AccountNumberPattern {

    String MESSAGE = "계좌번호는 11자 이상의 숫자만 입력할 수 있습니다.";

    String message() default MESSAGE;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}