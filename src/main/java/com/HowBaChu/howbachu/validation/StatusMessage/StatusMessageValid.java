package com.HowBaChu.howbachu.validation.StatusMessage;

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
@Constraint(validatedBy = {StatusMessageValidator.class})
@ExternalDocumentation(description = "상태메세지 형식이 맞는지 검사합니다.")
public @interface StatusMessageValid {
    String message() default "상태메세지는 30자까지 입력할 수 있습니다.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
