package ruby.shopping.common.valid;

import ruby.shopping.common.valid.validator.PageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = PageValidator.class)
@Documented
public @interface PagePattern {

    String MESSAGE = "페이지 값은 1 이상이어야 합니다.";

    String message() default MESSAGE;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}