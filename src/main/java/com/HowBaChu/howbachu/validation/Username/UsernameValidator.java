package com.HowBaChu.howbachu.validation.Username;

import com.HowBaChu.howbachu.validation.LimitBound;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameValid, String> {

    private final int minLength = LimitBound.userNameMinLength;
    private final int maxLength = LimitBound.userNameMaxLength;
    private final String message = "닉네임은 "+minLength+"자 이상 "+maxLength+"자 이하로 입력해 주세요.";

    @Override
    public void initialize(UsernameValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.length() > maxLength || value.length() < minLength) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }
}
