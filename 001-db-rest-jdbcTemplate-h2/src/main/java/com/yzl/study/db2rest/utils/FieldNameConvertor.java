package com.yzl.study.db2rest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

public class FieldNameConvertor {
	
	private static final String column_ = "_";
	
	/** _ 分割的数据库列名 转为 驼峰命名的 字段名
	 * @param columnName
	 * @return
	 */
	public static String columnName2FieldName(String columnName) {
		columnName=columnName.toLowerCase();
		StringBuilder sb = new StringBuilder();
		if(columnName.contains(column_)) {
			String[] ss = columnName.split(column_);
			for(String s:ss) {
				sb.append(StringUtils.capitalize(s));
			}
			
		}else {
			
			sb.append(StringUtils.capitalize(columnName));
		}
		return StringUtils.uncapitalize( sb.toString()) ;
	}
	
	
	
	/**  字段名转 列名可能会有问题  最好是从列名转为字段名后保存起对应关系
	 * @param fieldNamed
	 * @return
	 */
	public static String fieldNamed2ColumnName(String fieldNamed) {
		
		StringBuilder sb = new StringBuilder();
		int startInd = 0;
		for (int i=0 ;i<fieldNamed.length();i++) {
			  
			 if (Character.isUpperCase(fieldNamed.charAt(i))) {
			     
				 if(i>0) {
					 sb.append(fieldNamed.substring(startInd, i));
					 sb.append(column_);
					 startInd=i;
				 }
			   }
			 
		}
		if(startInd>0) {
			sb.append(fieldNamed.substring(startInd).toLowerCase());
		}else if(startInd==0) {
			return fieldNamed;
		}
		
		return  sb.toString().toLowerCase()  ;
	}
	
	
	public static List<Map<String, Object>>  columnName2FieldName(List<Map<String, Object>> list){
		
	    
		
		List<Map<String, Object>> queryForListResult =  new ArrayList<Map<String, Object>>();
		Map<String, Object>  tmp ;
		String tmpK ;
		for(Map<String, Object> m: list) {
			
			tmp = new HashMap<String, Object>();
			Iterator<String> iterator = m.keySet().iterator();
			while(iterator.hasNext()) {
				tmpK = iterator.next();
				tmp.put(FieldNameConvertor.columnName2FieldName(tmpK), m.get(tmpK));
			}
			
			queryForListResult.add(tmp);
			
		}
		return queryForListResult;
	}
	
	
	
	
	public static Map<String, Object>  columnName2FieldName (Map<String, Object> map){
		
		
		Map<String, Object>  tmp = new HashMap<String, Object>();
		String tmpK ;
			
			Iterator<String> iterator = map.keySet().iterator();
			while(iterator.hasNext()) {
				tmpK = iterator.next();
				tmp.put(FieldNameConvertor.columnName2FieldName(tmpK), map.get(tmpK));
			}
		return tmp;
	}
	
	
	public static Map<String, Object>  fieldName2columnName (Map<String, Object> map){
		
		
		Map<String, Object>  tmp = new HashMap<String, Object>();
		String tmpK ;
		
		Iterator<String> iterator = map.keySet().iterator();
		while(iterator.hasNext()) {
			tmpK = iterator.next();
			tmp.put(fieldNamed2ColumnName(tmpK), map.get(tmpK));
		}
		return tmp;
	}
	
	
	
	
	
public static void main(String[] args) {
	
	System.out.println("------------");
	System.out.println(columnName2FieldName("firt_name"));
	
}
}
