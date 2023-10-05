package com.HowBaChu.howbachu.validation.MBTI;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MBTIValidator implements ConstraintValidator<MBTIValid, MBTI> {
    private MBTIValid annotation;
    private final String message = "허용되지 않은 Enum값 입니다.";

    @Override
    public void initialize(MBTIValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MBTI value, ConstraintValidatorContext context) {
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value == enumValue) return true;
            }
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}
