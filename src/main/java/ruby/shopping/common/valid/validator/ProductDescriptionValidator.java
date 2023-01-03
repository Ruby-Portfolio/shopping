package ruby.shopping.common.valid.validator;

import ruby.shopping.common.valid.ProductDescriptionPattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProductDescriptionValidator implements ConstraintValidator<ProductDescriptionPattern, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !(value == null || value.isBlank()) && (value.length() <= 5000);
    }
}
