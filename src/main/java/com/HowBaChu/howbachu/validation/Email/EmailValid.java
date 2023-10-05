package com.HowBaChu.howbachu.validation.Email;

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
@ExternalDocumentation(description = "이메일 형식이 맞는지 검사합니다.",url = "https://howbachu.shop/swagger-ui.html#/User")
@Constraint(validatedBy = {EmailValidator.class})
public @interface EmailValid {
    String message() default "이메일 형식이 맞지 않습니다.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
