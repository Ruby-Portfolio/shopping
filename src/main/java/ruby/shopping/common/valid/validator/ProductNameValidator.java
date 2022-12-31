package ruby.shopping.common.valid.validator;

import ruby.shopping.common.valid.ProductNamePattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProductNameValidator implements ConstraintValidator<ProductNamePattern, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !(value == null || value.isBlank());
    }
}
