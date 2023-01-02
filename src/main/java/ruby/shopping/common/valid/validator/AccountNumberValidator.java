package ruby.shopping.common.valid.validator;

import ruby.shopping.common.valid.AccountNumberPattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountNumberValidator implements ConstraintValidator<AccountNumberPattern, String> {

    private static final String REGEXP = "^(\\d{11,})$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
