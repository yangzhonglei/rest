package com.yzl.study.db2rest.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.yzl.study.db2rest.component.DbMetaInfo;
import com.yzl.study.db2rest.component.SimpleRestRequest;
import com.yzl.study.db2rest.model.Order;
import com.yzl.study.db2rest.model.Sort;


public class MySqlBuilder {

	
	public static String buildCount(String tableName, Map<String, Object> conditionParaMap) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("select count(1) from "+tableName);
		sb.append(" where  1=1 ");
		sb.append(buildWhere(conditionParaMap));
		return sb.toString();
	}
	
	public static String buildSelect(String tableName, SimpleRestRequest request) {
		
		Map<String, Object> conditionParaMap = request.getConditionParaMap();
		Sort sort = null;
		Integer page =  null;
		Integer size =  null;
		if(request.getPage()!=null) {
			sort = request.getPage().getSort();
			page = request.getPage().getPage();
			size = request.getPage().getSize();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select * from "+tableName);
		sb.append(" where  1=1 ");
		sb.append(buildWhere(conditionParaMap));
		sb.append(buildOrderBy(sort));
		sb.append(buildlimit(page,size));
		
		return sb.toString();
	}
	
	
	public static String buildCreate(String tableName, Map<String, Object> obj) {
		//对于新增记录  若表里有  create_at字段--创建时间   怎自动设置为当前时间
		Date now = new Date();
				
		if(DbMetaInfo.checkColumnExist(tableName, "create_at")) {
			
			obj.put("create_at",now);
		}
		//对于新增记录  若表里有  update_at字段--最后更新时间   怎自动设置为当前时间
		if(DbMetaInfo.checkColumnExist(tableName, "update_at")) {
			
			obj.put("update_at",now);
		}
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
		return sb.toString();
	}
	
	
	public static String buildUpdate(String tableName, Map<String, Object> para) {
		 
		//对于更新记录  若表里有  update_at字段--最后更新时间   怎自动设置为当前时间
		if(DbMetaInfo.checkColumnExist(tableName, "update_at")) {
			
			para.put("update_at",new Date());
		}
		StringBuffer sb = new StringBuffer();
		sb.append("update  " + tableName);
		sb.append(" set ");
		for (Entry<String, Object> e : para.entrySet()) {
			sb.append(e.getKey() + " = :"+e.getKey() +",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" where id = :id");
		return sb.toString();
	}

//	public static String builderDelete(String tableName, SimpleRestRequest request) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	private static String buildWhere(Map<String, Object> conditionParaMap) {
		
		Map<String, Object> tmpMap = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		String trimedValue=null;
		for(Entry<String, Object> e :conditionParaMap.entrySet()) {
			if(!isKeyWord(e.getKey())) {
				if(!isKeyWord(e.getKey())) {
					
					if(e.getValue() instanceof String) {
						
						if( ((String)e.getValue())!=null) {
							trimedValue=((String)e.getValue()).trim();
							if("isNull".equals(trimedValue)) {
								
								sb.append(" and "+e.getKey()+" is null ");
							}else if("isNotNull".equals(trimedValue)) {
								
								sb.append(" and "+e.getKey()+" is not null ");
							}else if(trimedValue.startsWith("notEq$")) {
								
								sb.append(" and "+e.getKey()+" <> :"+e.getKey());
								tmpMap.put(e.getKey(), trimedValue.replace("notEq$", ""));
								
							}else {
								sb.append(" and "+e.getKey()+" = :"+e.getKey());
							}
							
						}
						
					}else {
						
						sb.append(" and "+e.getKey()+" = :"+e.getKey());
					}
				}
			}
			
		}
		
		conditionParaMap.putAll(tmpMap);
		return sb.toString();
	}
	
	
	
	private static String buildOrderBy(Sort sort) {
		
		StringBuffer sb = new StringBuffer();
		if(sort!=null && sort.getOrders()!=null && sort.getOrders().size()>0) {
			
			StringBuilder orderByStr = new StringBuilder();
			for(Order o: sort.getOrders()) {	
				if(o.isGbkconvert()) {
					// convert(name using gbk)
					orderByStr.append(" convert("+ o.getColName()+" using gbk) "+ o.getSortType()+",");
				}else {
					
					orderByStr.append(o.getColName()+" "+ o.getSortType()+",");
				}
			}
			orderByStr.deleteCharAt(orderByStr.length()-1);
			sb.append(" order by "+orderByStr.toString());
		}
		return sb.toString();
	}
	
	
	
	private static String buildlimit(Integer page , Integer size ) {
		
	 if(page == null ||  size == null) {
		 
		 return "";
		 
	 }else {
		 return " limit "+((page-1)*size)+" , " + size;
	 }
		
	}



    private static boolean isKeyWord(String kName) {
    	
    	if("page".equals(kName)  ||"size".equals(kName)||"sort".equals(kName) ) {
    		
    		return true ;
    	}
    	return false;
    }
    
    
    
    
	
}
