package com.woniu.base.db.sqlite;

import com.woniu.base.db.GenericSQLBuilder;
import com.woniu.base.db.ISQLBuilder;

public class SQLiteSQLBuilder extends GenericSQLBuilder implements ISQLBuilder {

	@Override
	public String getLastInsertIdSQL() {
		return "select last_insert_rowid()";
	}

	public void lock() {
		//no select xxx for update
	}
}
