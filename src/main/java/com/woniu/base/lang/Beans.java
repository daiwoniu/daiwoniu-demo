package com.woniu.base.lang;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import java.beans.PropertyDescriptor;
import java.util.Arrays;

public final class Beans {
	private Beans() {
	}

	public static void set(Object bean, String property, Object value) {
		BeanWrapper wrapper = PropertyAccessorFactory
				.forBeanPropertyAccess(bean);

		wrapper.setPropertyValue(property, value);
	}

	@SuppressWarnings("unchecked")
	public static <R> R get(Object bean, String property) {
		BeanWrapper wrapper = PropertyAccessorFactory
				.forBeanPropertyAccess(bean);
		return (R) wrapper.getPropertyValue(property);
	}

	public static <T, R> R copy(T from, Class<R> klass,
			String... excludedProperties) {
		R to = BeanUtils.instantiate(klass);
		return copy(from, to, excludedProperties);
	}

	/**
	 * 浅copy, 不逐级复制.
	 * 
	 * @param from
	 * @param to
	 * @param excludedProperties
	 * @return
	 */
	public static <T, R> R copy(T from, R to, String... excludedProperties) {
		BeanWrapper wrapper = PropertyAccessorFactory
				.forBeanPropertyAccess(from);
		BeanWrapper toWrapper = PropertyAccessorFactory
				.forBeanPropertyAccess(to);
        Arrays.sort(excludedProperties);

		for (PropertyDescriptor descriptor : wrapper.getPropertyDescriptors()) {
			if (descriptor.getWriteMethod() == null
					|| descriptor.getReadMethod() == null) {
				continue;
			}

			String name = descriptor.getName();
			boolean found = Arrays.binarySearch(excludedProperties, name) >= 0;
			if (found) {
				continue;
			}

			toWrapper.setPropertyValue(name, wrapper.getPropertyValue(name));
		}
		return to;
	}

	public static <T, R> T extend(T target, R source, String... includedProperties) {
		return extend(target, source, false, includedProperties);
	}

    /**
     *
     * @param target
     * @param source
	 * @param ignoreNullProperty 是否忽略source里值为空的属性
     * @param includedProperties 未指定时extend所有属性
     * @param <T>
     * @param <R>
     * @return
     */
	public static <T, R> T extend(T target, R source, boolean ignoreNullProperty,
			String... includedProperties) {
		BeanWrapper targetWrapper = PropertyAccessorFactory
				.forBeanPropertyAccess(target);
		BeanWrapper sourceWrapper = PropertyAccessorFactory
				.forBeanPropertyAccess(source);

        Arrays.sort(includedProperties);

		for (PropertyDescriptor descriptor : sourceWrapper
				.getPropertyDescriptors()) {
			if (descriptor.getWriteMethod() == null
					|| descriptor.getReadMethod() == null) {
				continue;
			}

			String name = descriptor.getName();
			boolean found = includedProperties.length == 0
					|| Arrays.binarySearch(includedProperties, name) >= 0;
			if (!found) {
				continue;
			}

			if (ignoreNullProperty && sourceWrapper.getPropertyValue(name) == null) {
				continue;
			}
			
			if (!targetWrapper.isWritableProperty(name)) {
				continue;
			}
			
			targetWrapper.setPropertyValue(name,
					sourceWrapper.getPropertyValue(name));
		}
		return target;
	}
}
