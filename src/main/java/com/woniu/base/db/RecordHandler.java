package com.woniu.base.db;

public interface RecordHandler<T> {

	void process(T record);

}
