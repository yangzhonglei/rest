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

@Component
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
		 Integer page = request.getPage().getPage();
		 Integer size = request.getPage().getSize();
		 String selectSql = "select * from "+tableName ;
		 
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select * from "+tableName);
			sb.append(" where  1=1 ");
			for(Entry<String, Object> e :conditionParaMap.entrySet()) {
				if(!isKeyWord(e.getKey())) {
					sb.append(" and "+e.getKey()+" = :"+e.getKey());
				}
				
			}
			
			Sort st = request.getPage().getSort();
			if(st!=null && st.getOrders()!=null && st.getOrders().size()>0) {
			
				StringBuilder orderByStr = new StringBuilder();
				for(Order o: st.getOrders()) {	
					orderByStr.append(o.getColName()+" "+ o.getSortType()+",");
				}
				orderByStr.deleteCharAt(orderByStr.length()-1);
				sb.append(" order by "+orderByStr.toString());
			}
			sb.append(" limit "+((page-1)*size)+" , " + page*size);
			System.out.println("================"+sb.toString());
			List<Map<String, Object>> queryForList = namedParameterJdbcTemplate.queryForList(sb.toString(), conditionParaMap);
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
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select count(1) from "+tableName);
			sb.append(" where  1=1 ");
			for(Entry<String, Object> e :conditionParaMap.entrySet()) {
				if(!isKeyWord(e.getKey())) {
					sb.append(" and "+e.getKey()+" = :"+e.getKey());
				}
				
			}
			System.out.println("================"+sb.toString());
			count =  namedParameterJdbcTemplate.queryForObject(sb.toString(), conditionParaMap, Integer.class) ;
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
	public int delete(String tableName ,Integer id) {
		 
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
    private TransactionStatus beginTransaction(DataSourceTransactionManager transactionManager){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();//事务定义类
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);// 返回事务对象
        return status;
    }
    
    
    private boolean isKeyWord(String kName) {
    	
    	if("page".equals(kName)  ||"size".equals(kName)||"sort".equals(kName) ) {
    		
    		return true ;
    	}
    	return false;
    }


    
    
}
