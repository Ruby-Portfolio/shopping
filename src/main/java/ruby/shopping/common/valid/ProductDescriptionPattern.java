package ruby.shopping.common.valid;

import ruby.shopping.common.valid.validator.ProductDescriptionValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = ProductDescriptionValidator.class)
@Documented
public @interface ProductDescriptionPattern {

    String MESSAGE = "상품 설명은 필수값입니다.";

    String message() default MESSAGE;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}