package com.woniu.base.db.mysql;

import com.woniu.base.db.GenericSQLBuilder;
import com.woniu.base.db.ISQLBuilder;

public class MySQLSQLBuilder extends GenericSQLBuilder implements ISQLBuilder {
	
	@Override
	public String getLastInsertIdSQL() {
		return "select last_insert_id()";
	}

}
