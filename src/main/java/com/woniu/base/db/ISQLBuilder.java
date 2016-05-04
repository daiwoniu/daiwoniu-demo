package com.woniu.base.db;

public interface ISQLBuilder extends Cloneable {
	ISQLBuilder clone();

	void setTable(String table);

	Object[] getParameters();

	String toCountSQL();

	String toSQL();

	String escapeColumn(String column);

    void tag(String tag);

	void where(String condition, Object... params);

	/**
	 * 支持or条件查询
	 * @param orKey 判断一组or查询的key
	 * @param condition 条件列名
	 * @param params 条件值
	 */
	void where(String orKey, String condition, Object... params);

	void select(String... columns);

	void clearSelect();

	void join(String joinSql);

	void groupBy(String groupBy);

	void having(String having);

	void orderBy(String orderBy);

	void limit(Integer offset, Integer rowCount);

	boolean hasLimit();

	void lock();

	String getLastInsertIdSQL();
}