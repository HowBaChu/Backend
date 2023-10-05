package com.HowBaChu.howbachu.validation.MBTI;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;


@ExternalDocumentation(description = "Enum에 있는 값인지 검사합니다.",url = "https://howbachu.shop/swagger-ui.html#/User")
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = {MBTIValidator.class})
public @interface MBTIValid {
    String message() default "존재하지 않는 MBTI 입니다.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    Class<? extends java.lang.Enum<?>> enumClass();

}