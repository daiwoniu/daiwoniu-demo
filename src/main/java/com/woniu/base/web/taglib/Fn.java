package com.woniu.base.web.taglib;

import java.util.Collection;

public final class Fn {

	private Fn() {
	}

	public static boolean contains(Collection<Object> container, Object object) {
		return container.contains(object);
	}
	
}
