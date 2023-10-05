package com.HowBaChu.howbachu.validation.Email;

import com.HowBaChu.howbachu.repository.MemberRepository;
import com.HowBaChu.howbachu.validation.LimitBound;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailValidator implements ConstraintValidator<EmailValid,String> {

    private final MemberRepository memberRepository;
    private final int maxLength = LimitBound.emailMaxLength;
    private final int minLength = LimitBound.emailMinLength;
    private final String lengthMessage = "이메일은 "+minLength+"자 이상 "+maxLength+"자 이하로 입력해 주세요.";
    private final String formatMessage = "이메일 형식이 올바르지 않습니다.";
    private final String duplicateMessaga = "이미 가입된 이메일입니다.";
    private final String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    @Override
    public void initialize(EmailValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value.length() > maxLength || value.length() < minLength) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(lengthMessage).addConstraintViolation();
            return false;
        }
        if (!value.matches(regex)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(formatMessage).addConstraintViolation();
            return false;
        }

        if (memberRepository.existsByEmail(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(duplicateMessaga).addConstraintViolation();
            return false;
        }

        return true;
    }
}
