package com.yzl.study.db2rest.dao;

import java.util.List;
import java.util.Map;

import com.yzl.study.db2rest.component.SimpleRestRequest;

public interface ITableCRUD  {

	/**创建一条数据库表记录
	 * @param tableName 表名
	 * @param obj       参数map
	 * @return
	 */
	int create(String tableName, Map<String,Object> obj);

	/**根据表主键查询 记录 ,没有单列的主键的话 则根据id列查询
	 * @param tableName  表名
	 * @param id         主键值
	 * @return
	 */
	Map<String, Object> retrieve(String tableName,Object id);

	
	/** 查询所有的记录,并更加参数map条件过滤
	 * @param tableName
	 * @param para
	 * @return
	 */
	List<Map<String, Object>> retrieve(String tableName,SimpleRestRequest request );
	
	/** 查询符合条件的记录的行数
	 * @param tableName
	 * @param para
	 * @return
	 */
	Integer count(String tableName,SimpleRestRequest request );
	

	/** 根据表主键更新表记录
	 * @param tableName 表名
	 * @param id        表的主键 值
	 * @param para      参数map
	 * @return
	 */
	int update(String tableName ,Object id ,Map<String, Object> para);

	/**根据表主键 删除 记录 
	 * @param tableName 表名
	 * @param id        主键值
	 * @return
	 */
	int delete(String tableName ,Integer id);



}