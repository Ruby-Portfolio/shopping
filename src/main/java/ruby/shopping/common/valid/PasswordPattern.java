package ruby.shopping.common.valid;

import ruby.shopping.common.valid.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface PasswordPattern {

    String MESSAGE = "비밀번호는 8~50자 영문, 숫자, 특수문자만 입력할 수 있습니다. 비밀번호는 적어도 하나 이상의 영문, 숫자, 특수문자를 모두 포함해야 합니다.";

    String message() default MESSAGE;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}