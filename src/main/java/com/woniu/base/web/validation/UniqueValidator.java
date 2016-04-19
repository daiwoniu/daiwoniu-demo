package com.woniu.base.web.validation;

import com.woniu.base.db.Query;
import com.woniu.base.db.DB;
import com.woniu.base.db.EntityWrapper;
import com.woniu.base.db.IDBProvider;
import com.google.common.base.Strings;
import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {
	private IDBProvider dbProvider;
	private String property;
	private String condition;
	private String[] conditionParameterProperties;

	@Override
	public void initialize(Unique constraintAnnotation) {
		this.property = constraintAnnotation.property();
		Class<? extends IDBProvider> klass = constraintAnnotation.dbProvider();
		dbProvider = BeanUtils.instantiate(klass);
		condition = constraintAnnotation.condition();
		conditionParameterProperties = constraintAnnotation
				.conditionParameterProperties();
	}

	@Override
	public boolean isValid(Object entity, ConstraintValidatorContext context) {
		EntityWrapper wrapper = new EntityWrapper(entity);
		Object propertyValue = wrapper.getPropertyValue(property);

		if (propertyValue == null || "".equals(propertyValue)) {
			return true;
		}

		DB db = dbProvider.get();
		Query query = db.from(wrapper.getTableName()).select("1")
				.where(wrapper.getColumnName(property), propertyValue);

		if (!Strings.isNullOrEmpty(condition)) {
			List<Object> parameters = new ArrayList<Object>();
			for (String p : conditionParameterProperties) {
				parameters.add(wrapper.getPropertyValue(p));
			}
			query.segment(condition, parameters.toArray());
		}
		Object id = wrapper.getId();
		if (id != null) {
			query.not(wrapper.getIdColumnName(), id);
		}

		if (query.isExists()) {
			context.buildConstraintViolationWithTemplate("已存在")
					.addPropertyNode(property).addConstraintViolation()
					.disableDefaultConstraintViolation();
			return false;
		}

		return true;
	}
}
