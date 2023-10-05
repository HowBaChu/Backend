package com.HowBaChu.howbachu.validation.Password;

import com.HowBaChu.howbachu.validation.LimitBound;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {

    private final int maxLength = LimitBound.passwordMaxLength;
    private final int minLength = LimitBound.passwordMinLength;
    private final String regex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{"+minLength+","+maxLength+"}$";
    private final String message = "비밀번호는 영어, 숫자, 특수 문자를 포함한 "+minLength+"자 이상 "+maxLength+"자 이하로 입력해주세요.";
    @Override
    public void initialize(PasswordValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!value.matches(regex)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }
}
