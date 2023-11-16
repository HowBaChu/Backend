package com.HowBaChu.howbachu.validation.Username;

import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.validation.LimitBound;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernameValidator implements ConstraintValidator<UsernameValid, String> {

    private final MemberRepository memberRepository;
    private final int minLength = LimitBound.userNameMinLength;
    private final int maxLength = LimitBound.userNameMaxLength;
    private final String lengthMessage = "닉네임은 "+minLength+"자 이상 "+maxLength+"자 이하로 입력해 주세요.";

    @Override
    public void initialize(UsernameValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.length() > maxLength || value.length() < minLength) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(lengthMessage).addConstraintViolation();
            return false;
        }
        return true;
    }
}
