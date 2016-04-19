package com.woniu.base.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPValidator implements ConstraintValidator<IP, CharSequence> {
	private final static Pattern IP_PATTERN = Pattern
			.compile("^(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)$");

	@Override
	public void initialize(IP constraintAnnotation) {
	}

	@Override
	public boolean isValid(CharSequence value,
			ConstraintValidatorContext context) {
		if (value == null || value.length() == 0) {
			return true;
		}
		Matcher m = IP_PATTERN.matcher(value);
		if (m.find()) {
			for (int i = 0; i < 4; i++) {
				String s = m.group(i + 1);
				int num = Integer.parseInt(s);
				if (num > 255) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}