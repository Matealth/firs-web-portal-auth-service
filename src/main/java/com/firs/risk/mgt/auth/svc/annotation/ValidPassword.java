package com.firs.risk.mgt.auth.svc.annotation;

import com.firs.risk.mgt.auth.svc.validator.PasswordConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidPassword {

    String message() default "Password must be at least 8 characters, contains uppercase, " +
            "lowercase, number and special characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}