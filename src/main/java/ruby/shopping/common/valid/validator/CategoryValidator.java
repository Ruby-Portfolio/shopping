package ruby.shopping.common.valid.validator;

import ruby.shopping.common.valid.CategoryPattern;
import ruby.shopping.domain.product.enums.Category;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class CategoryValidator implements ConstraintValidator<CategoryPattern, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Arrays.stream(Category.values())
                .anyMatch(category -> category.name().equals(value));
    }
}
