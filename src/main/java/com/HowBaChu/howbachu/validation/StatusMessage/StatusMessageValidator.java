package com.HowBaChu.howbachu.validation.StatusMessage;

import com.HowBaChu.howbachu.validation.LimitBound;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StatusMessageValidator implements ConstraintValidator<StatusMessageValid, String> {
    private final int maxLength = LimitBound.statusMessageMaxLength;
    public final String message = "상태메세지는 "+maxLength+"자 이하로 입력해 주세요.";

    @Override
    public void initialize(StatusMessageValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.length() > maxLength) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }
        return true;
    }
}
