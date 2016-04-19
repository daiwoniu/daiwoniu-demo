package com.woniu.base.web.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConfirmValidator implements ConstraintValidator<Confirm, Object> {
	private String property;
	private String confirmProperty;

	@Override
	public void initialize(Confirm constraintAnnotation) {
		property = constraintAnnotation.property();
		confirmProperty = constraintAnnotation.confirmProperty();
	}

	@Override
	public boolean isValid(Object entity, ConstraintValidatorContext context) {
		BeanWrapper wrapper = PropertyAccessorFactory
				.forBeanPropertyAccess(entity);
		Object value = wrapper.getPropertyValue(property);
		Object confirmValue = wrapper.getPropertyValue(confirmProperty);

		if (isNullOrEmpty(value) && isNullOrEmpty(confirmValue)) {
			return true;
		}

		if (value != null && value.equals(confirmValue)) {
			return true;
		}

		context.buildConstraintViolationWithTemplate("两次输入不一致")
				.addPropertyNode(confirmProperty).addConstraintViolation()
				.disableDefaultConstraintViolation();
		return false;
	}

	private boolean isNullOrEmpty(Object obj) {
		return obj == null || "".equals(obj);
	}

}
