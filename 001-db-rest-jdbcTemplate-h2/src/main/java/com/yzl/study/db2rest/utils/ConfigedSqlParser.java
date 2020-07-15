package com.yzl.study.db2rest.utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.springframework.util.DigestUtils;

public class ConfigedSqlParser {
	
	private static JexlEngine jexl = new JexlBuilder().create();
	
	private static Map<String,String> sqlCache = new ConcurrentHashMap<String,String>();
			
	 
	//TODO  怎么处理    字段不出现的问题  GroovyShell 问题
	public static void main(String[] args) {
	       String s = 
	    	        "SELECT * FROM t_employee t\n" + 
	    	        "where 1=1\n" + 
	    	        "<if test=\"delete != null\">\n" + 
	    	        "and delete= :delete\n" + 
	    	        "</if>\n" + 
	    	        "<if test=\"dept_name != null\">\n" + 
	    	        "and dept_name= :dept_name\n" + 
	    	        "</if>\n";
       
		 Map<String, Object> paraMap =  new HashMap<String, Object>();
		 paraMap.put("delete", "11");
		 paraMap.put("dept_name", "Groovy");
		 
		 long b = System.currentTimeMillis();
//		 for(int i=0;i<1000000;i++) {
//			 
//			 parse(s,paraMap);
//		 }
		 long e = System.currentTimeMillis();
		 
//		 System.out.println(e-b);
		String sql =  parse(s,paraMap);
		
		
		System.out.println("===========================");	 
//		System.out.println(sql);	 
		 
	}
	
	
	public static String parse(String configedSqlText, Map<String, Object> paraMap) {

		if (configedSqlText == null || configedSqlText.trim().length() == 0) {
            
			return null;
		}
	
	
		 Map<String, Object> treeMap = new TreeMap<String, Object>();
		 treeMap.putAll(paraMap);
		 StringBuilder fullKeyStr =  new StringBuilder();
		 
		 Iterator<String> it = treeMap.keySet().iterator();
		 while(it.hasNext()) {
			 fullKeyStr.append(it.next());
		 }
		 String md5 = DigestUtils.md5DigestAsHex((configedSqlText +  fullKeyStr.toString()).getBytes(Charset.forName("UTF-8")));
		 
         if( sqlCache.containsKey(md5)) {
        	 
        	 return sqlCache.get(md5);
         }
		
		StringBuilder sqlSb = new StringBuilder();
		Map<String, Object> varMap = new HashMap<String, Object>();
		
		
		int configedSqlTextLength = configedSqlText.length();
		int firstLeftArrowIndex = configedSqlText.indexOf("<if");
		List<Integer> leftArrowList = new ArrayList<Integer>();
		if (firstLeftArrowIndex > -1) {  //有出现
			leftArrowList.add(firstLeftArrowIndex);
			int tmp = firstLeftArrowIndex;
			while (tmp != -1 && tmp < configedSqlTextLength) {
//				System.out.println(tmp);
				tmp = configedSqlText.indexOf("<if", tmp + 1);
				if (tmp != -1) {
					leftArrowList.add(tmp);
				}

			}
		}

		if (leftArrowList.size() > 0) {

			sqlSb.append(configedSqlText.substring(0, leftArrowList.get(0)));

			for (int i : leftArrowList) {

				int nextRightArrow = configedSqlText.indexOf(">", i);
				int nextEq = configedSqlText.indexOf("=", i);
				int endIfIndex = configedSqlText.indexOf("</if>", i);

				String test = configedSqlText.substring(nextEq + 1, nextRightArrow);
				test = test.replace("\"", "");
				test = test.trim();
				int blankSpaceIndex = test.indexOf(" ", 0);
				String field = test.substring(0, blankSpaceIndex);
//            	System.out.println(field);
				varMap.put(field, null);
//				System.out.println(test);
				
				JexlExpression e = jexl.createExpression( test );
				JexlContext jc = new MapContext();
				
				// 便利一遍 varMap 和 binding 不存在的 设置为 null
				Iterator<String> iterator = varMap.keySet().iterator();
				String tmpK = null;
				while (iterator.hasNext()) {
					tmpK = iterator.next();
					if (paraMap.get(tmpK) == null) {

						jc.set(tmpK, null);
					}else {
						jc.set(tmpK, paraMap.get(tmpK));
					}
				}
				Object value = e.evaluate(jc);
				
				boolean b = (boolean) value;
				if (b) {
					sqlSb.append(configedSqlText.substring(nextRightArrow + 1, endIfIndex));
				}
			}
		}else {
			
			sqlSb.append(configedSqlText);
		}
		
		
		sqlCache.put(md5, sqlSb.toString());
		return sqlSb.toString();
	}
	
	

}
