package com.woniu.base.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DomainValidator implements
		ConstraintValidator<Domain, CharSequence> {

	private static final Pattern DOMAIN_PATTERN = Pattern.compile("^[\\w-\\.]+$");

	@Override
	public void initialize(Domain constraintAnnotation) {
	}

	@Override
	public boolean isValid(CharSequence value,
			ConstraintValidatorContext context) {
		if (value == null || value.length() <= 0) {
			return false;
		}
		Matcher m = DOMAIN_PATTERN.matcher(value);
		if (m.matches()) {
			return true;
		}
		return false;
	}
}
