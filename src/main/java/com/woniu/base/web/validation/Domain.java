package com.woniu.base.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, PARAMETER, CONSTRUCTOR, ANNOTATION_TYPE })
@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = { DomainValidator.class })
public @interface Domain {

	String message() default "域名不正确";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
