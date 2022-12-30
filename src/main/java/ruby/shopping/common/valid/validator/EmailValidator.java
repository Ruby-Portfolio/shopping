package ruby.shopping.common.valid.validator;

import ruby.shopping.common.valid.EmailPattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailPattern, String> {

    private static final String REGEXP = "^[\\da-zA-Z]([-_.]?[\\da-zA-Z])*@[\\da-zA-Z]([-_.]?[\\da-zA-Z])*.[a-zA-Z]{2,3}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile(REGEXP);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
}
