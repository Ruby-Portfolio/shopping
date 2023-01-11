package ruby.shopping.common.valid.validator;

import ruby.shopping.common.valid.PagePattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PageValidator implements ConstraintValidator<PagePattern, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value >= 1;
    }
}
