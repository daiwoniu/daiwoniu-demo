package com.woniu.base.db;

import com.woniu.base.util.ApplicationContextProvider;

public class SpringDBProvider implements IDBProvider {

	@Override
	public DB get() {
		return ApplicationContextProvider.getApplicationContext().getBean(
				DB.class);
	}

}
