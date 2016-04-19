package com.woniu.base.web.validation;

import com.woniu.base.db.SpringDBProvider;
import com.woniu.base.db.IDBProvider;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { UniqueValidator.class })
@Documented
public @interface Unique {

	String message() default "已存在";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	Class<? extends IDBProvider> dbProvider() default SpringDBProvider.class;

	String property();

	String condition() default "";

	String[] conditionParameterProperties() default {};

	@Target({ TYPE, ANNOTATION_TYPE })
	@Retention(RUNTIME)
	@Documented
	@interface List {
		Unique[] value();
	}

}
