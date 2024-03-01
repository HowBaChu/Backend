package com.HowBaChu.howbachu.validation.Password;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target(ElementType.FIELD)
@Constraint(validatedBy = {PasswordValidator.class})
@ExternalDocumentation(description = "비밀번호 형식이 맞는지 검사합니다. ")
public @interface PasswordValid {
    String message() default "비밀번호 형식이 맞지 않습니다.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
