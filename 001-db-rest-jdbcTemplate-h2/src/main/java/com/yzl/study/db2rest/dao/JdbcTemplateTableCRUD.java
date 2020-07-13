package com.yzl.study.db2rest.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.yzl.study.db2rest.component.DbMetaInfo;
import com.yzl.study.db2rest.component.SimpleRestRequest;
import com.yzl.study.db2rest.component.TableInfo;
import com.yzl.study.db2rest.model.Order;
import com.yzl.study.db2rest.model.Sort;
import com.yzl.study.db2rest.utils.FieldNameConvertor;
import com.yzl.study.db2rest.utils.MySqlBuilder;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JdbcTemplateTableCRUD implements ITableCRUD {
	
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	DataSourceTransactionManager dataSourceTransactionManager;
	
	
	@Override
	public int create(String tableName, Map<String, Object> obj) {

		int update;
		TransactionStatus status = null;
		try {
			String toBeExeSql =  MySqlBuilder.buildCreate(tableName, obj);
			log.debug("=== to be exe sql:{}",toBeExeSql);
			status = beginTransaction(dataSourceTransactionManager);
			update = namedParameterJdbcTemplate.update(toBeExeSql, obj);
			dataSourceTransactionManager.commit(status);
		} catch (Exception e) {
			update = -100;
			e.printStackTrace();
			dataSourceTransactionManager.rollback(status);
		}

		return update;

	}

	@Override
	public Map<String, Object> retrieve(String tableName ,Object id) {
		
		try {
			TableInfo  table=DbMetaInfo.tableMap.get(tableName);
			String pk = null;
			if(table.getSinglePkColName()!=null) {//单列主键时主键查询
				pk=table.getSinglePkColName();
			}else {
				pk = "id";//不能存在主键时 用id列查询   --- 可能导致报错  TODO 异常处理
			}
			
			Map<String,Object> para = new HashMap<String,Object>();
			para.put(pk, id);
			List<Map<String, Object>> queryForList = namedParameterJdbcTemplate.queryForList("select * from "+tableName + " where "+pk+" = :"+pk,para);
			if(queryForList!=null && queryForList.size()>0) {
				return  FieldNameConvertor.columnName2FieldName(queryForList.get(0));
			}else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	
		
	}

	@Override
	public List<Map<String, Object>> retrieve(String tableName ,SimpleRestRequest request ) {
		 
		 Map<String, Object> conditionParaMap = request.getConditionParaMap();
		try {
			String toBeExeSql =  MySqlBuilder.buildSelect(tableName, request);
			log.debug("=== to be exe sql:{}",toBeExeSql);
			List<Map<String, Object>> queryForList = namedParameterJdbcTemplate.queryForList(toBeExeSql, conditionParaMap);
			return FieldNameConvertor.columnName2FieldName(queryForList);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public Integer count(String tableName, SimpleRestRequest request) {
		 
		 Integer count = 0;
		 Map<String, Object> conditionParaMap = request.getConditionParaMap();
		 Map<String, Object> newParaMap = new HashMap<String, Object>();
		 for(Map.Entry<String, Object> e:  conditionParaMap.entrySet()   ) {
			 
			 newParaMap.put(e.getKey(), e.getValue());
		 }
		 
		try {
			String toBeExeSql = MySqlBuilder.buildCount(tableName, newParaMap);
			log.debug("=== to be exe sql:{}",toBeExeSql);
			count =  namedParameterJdbcTemplate.queryForObject(toBeExeSql, newParaMap, Integer.class) ;
			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return count;
		}
	}
    
	
	
	@Override
	public int update(String tableName ,Object id,Map<String, Object> para) {
		
		
		int update;
		TransactionStatus status = null;
		try {
			para.put("id", id);
			String toBeExeSql = MySqlBuilder.buildUpdate(tableName, para);
			log.debug("=== to be exe sql:{}",toBeExeSql);
			status = beginTransaction(dataSourceTransactionManager);
			update = namedParameterJdbcTemplate.update(toBeExeSql, para);
			dataSourceTransactionManager.commit(status);
		} catch (Exception e) {
			update = -100;
			e.printStackTrace();
			dataSourceTransactionManager.rollback(status);
		}

		return update;
	}

	
	@Override
	public int delete(String tableName ,Integer id) {
		 
		int update;
		TransactionStatus status = null;
		Map<String,Object> para = new HashMap<String,Object>();
		para.put("id", id);
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("delete from  " + tableName);
			sb.append(" where id = :id");
			String toBeExeSql = sb.toString();
			log.debug("=== to be exe sql:{}",toBeExeSql);
			status = beginTransaction(dataSourceTransactionManager);
			update = namedParameterJdbcTemplate.update(toBeExeSql, para);
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
    private TransactionStatus beginTransaction(DataSourceTransactionManager transactionManager){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();//事务定义类
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);// 返回事务对象
        return status;
    }
    
    


    
    
}
