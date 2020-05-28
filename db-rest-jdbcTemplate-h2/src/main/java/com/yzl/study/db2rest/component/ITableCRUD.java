package com.yzl.study.db2rest.component;

import java.util.Map;

public interface ITableCRUD  {

	
	Object create(String tableName, Map<String,Object> obj);

	Object retrieve(String tableName,Integer id);
	
	Object retrieveAll(String tableName);

	Object update(String tableName ,Integer id ,Map<String, Object> para);

	Object delete(String tableName ,Integer id);

	Object retrieveAll(String tableName, Map<String, Object> para);
	

}