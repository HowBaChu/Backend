package com.HowBaChu.howbachu.validation.MBTI;

import com.HowBaChu.howbachu.domain.constants.MBTI;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MBTIValidator implements ConstraintValidator<MBTIValid, MBTI> {

    @Override
    public void initialize(MBTIValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MBTI value, ConstraintValidatorContext context) {
        return value != null;
    }
}
