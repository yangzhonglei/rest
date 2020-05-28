package com.yzl.study.db2rest.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class TableCRUD implements ITableCRUD {
	
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	DataSourceTransactionManager dataSourceTransactionManager;
	
	
	
	@Override
	public Object create(String tableName, Map<String, Object> obj) {

		int update;
		TransactionStatus status = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO " + tableName);
			sb.append("(");
			for (Entry<String, Object> e : obj.entrySet()) {
				sb.append(e.getKey() + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(") VALUES (");
			for (Entry<String, Object> e : obj.entrySet()) {
				sb.append(" :" + e.getKey() + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(")");
			status = beginTransaction(dataSourceTransactionManager);
			update = namedParameterJdbcTemplate.update(sb.toString(), obj);
			dataSourceTransactionManager.commit(status);
		} catch (Exception e) {
			update = -100;
			e.printStackTrace();
			dataSourceTransactionManager.rollback(status);
		}

		return update;

	}

	@Override
	public Object retrieve(String tableName ,Integer id) {
		
		List<Map<String, Object>> queryForList = jdbcTemplate.queryForList("select * from "+tableName + " where id = " + id);
		if(queryForList!=null && queryForList.size()>0) {
			return queryForList.get(0);
		}else {
			return null;
		}
		
	}

	@Override
	public Object retrieveAll(String tableName ,Map<String, Object> para) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("select * from "+tableName);
		sb.append(" where  1=1 ");
		for(Entry<String, Object> e :para.entrySet()) {
			sb.append(" and "+e.getKey()+" = :"+e.getKey());
		}
		List<Map<String, Object>> queryForList = namedParameterJdbcTemplate.queryForList(sb.toString(), para);
		return queryForList;
	}

	
	@Override
	public Object retrieveAll(String tableName) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from "+tableName);
		Map<String,Object> para = new HashMap<String,Object>();
		List<Map<String, Object>> queryForList = namedParameterJdbcTemplate.queryForList(sb.toString(), para);
		return queryForList;
	}

	
	
	
	@Override
	public Object update(String tableName ,Integer id,Map<String, Object> para) {
		
		
		int update;
		TransactionStatus status = null;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("update  " + tableName);
			sb.append(" set ");
			for (Entry<String, Object> e : para.entrySet()) {
				sb.append(e.getKey() + " = :"+e.getKey() +",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(" where id = :id");
			
			para.put("id", id);
			status = beginTransaction(dataSourceTransactionManager);
			update = namedParameterJdbcTemplate.update(sb.toString(), para);
			dataSourceTransactionManager.commit(status);
		} catch (Exception e) {
			update = -100;
			e.printStackTrace();
			dataSourceTransactionManager.rollback(status);
		}

		return update;
	}

	@Override
	public Object delete(String tableName ,Integer id) {
		 
		int update;
		TransactionStatus status = null;
		Map<String,Object> para = new HashMap<String,Object>();
		para.put("id", id);
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("delete from  " + tableName);
			sb.append(" where id = :id");
			status = beginTransaction(dataSourceTransactionManager);
			update = namedParameterJdbcTemplate.update(sb.toString(), para);
			dataSourceTransactionManager.commit(status);
		} catch (Exception e) {
			update = -100;
			e.printStackTrace();
			dataSourceTransactionManager.rollback(status);
		}

		return update;
	}


	
	
	
	/**
     * 开启事务
     */
    public TransactionStatus beginTransaction(DataSourceTransactionManager transactionManager){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();//事务定义类
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);// 返回事务对象
        return status;
    }
 
    /**
     * 提交事务
     * @param transactionManager
     * @param status
     */
    public void commitTransaction(DataSourceTransactionManager transactionManager,TransactionStatus status){
        transactionManager.commit(status);
    }
 
    /**
     * 事务回滚
     * @param transactionManager
     * @param status
     */
    public void rollbackTransaction(DataSourceTransactionManager transactionManager,TransactionStatus status){
        transactionManager.rollback(status);
    }
	
	
	
	
	
	
}
