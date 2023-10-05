package com.HowBaChu.howbachu.validation.Username;

import static com.HowBaChu.howbachu.validation.LimitBound.userNameMaxLength;
import static com.HowBaChu.howbachu.validation.LimitBound.userNameMinLength;

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
@Constraint(validatedBy = {UsernameValidator.class})
@ExternalDocumentation(description = "이름 형식이 맞는지 검사합니다.",url = "https://howbachu.shop/swagger-ui.html#/User")
public @interface UsernameValid {
    String message() default "이름은 (" + userNameMinLength + "~" + userNameMaxLength + ")자 이어야 합니다.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
