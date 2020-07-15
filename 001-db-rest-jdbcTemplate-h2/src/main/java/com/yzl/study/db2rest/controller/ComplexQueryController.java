package com.yzl.study.db2rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yzl.study.db2rest.component.ComplexQueryExecuter;
import com.yzl.study.db2rest.component.ComplexQueryHolder;
import com.yzl.study.db2rest.model.ComplexQuery;
import com.yzl.study.db2rest.model.Order;
import com.yzl.study.db2rest.model.Page;
import com.yzl.study.db2rest.model.PageResponse;
import com.yzl.study.db2rest.model.ResponseMessage;
import com.yzl.study.db2rest.model.Sort;
import com.yzl.study.db2rest.utils.FieldNameConvertor;
import com.yzl.study.db2rest.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ComplexQueryController {
	
	@Autowired
	ComplexQueryHolder complexQueryHolder;
	
	@Autowired
	ComplexQueryExecuter complexQueryExecuter;
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping(value ="/query/**",produces = "application/json;charset=utf-8")
	public  Object query(HttpServletRequest req,@RequestBody Map<String,Object> reqMap) {
		
		Page  page = parsePage(reqMap);
		String method = req.getMethod();
		String path = req.getServletPath();
		log.info("===query page:{} ,reqMap:{}", new Object[]{ JsonUtils.toJson(page), JsonUtils.toJson(reqMap)});
		
		ComplexQuery c = complexQueryHolder.matchComplexQuery(method, path);
		if(c==null) {
			log.info("===query response:{}", JsonUtils.toJson(ResponseMessage.failMsg()));
			return ResponseMessage.failMsg();
		}
		
		
		Integer count = complexQueryExecuter.count(c, reqMap); 
		if(count!=null &&count==0) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			PageResponse prsb = new PageResponse();
			prsb.setList(list);
			prsb.setPage(page.getPage());
			prsb.setTotal((long)count);
			prsb.setSize(page.getSize());
			log.info("===query response:{}", JsonUtils.toJson(ResponseMessage.successMsg(prsb)));
			return ResponseMessage.successMsg(prsb);
		}
		if(count!=null &&count>0) {
			List<Map<String, Object>> list = complexQueryExecuter.query(c, reqMap,page);
			if(list==null) {
				list = new ArrayList<Map<String, Object>> ();
			}
			PageResponse prsb = new PageResponse();
			prsb.setList(list);
			prsb.setPage(page.getPage());
			prsb.setTotal((long)count);
			prsb.setSize(page.getSize());
			log.info("===query response:{}", JsonUtils.toJson(ResponseMessage.successMsg(prsb)));
			return ResponseMessage.successMsg(prsb);
			
		}
		log.info("===query response:{}", JsonUtils.toJson( ResponseMessage.failMsg()));
		return ResponseMessage.failMsg();
		
		
	}


	private Page parsePage(Map<String, Object> reqMap) {
		Page  page = new Page();
	    if(reqMap.get("page")!=null) {
	    	  Integer p=   (Integer) reqMap.get("page");
	    	  page.setPage(p);
	    }
	    if(reqMap.get("size")!=null) {
	    	Integer s=   (Integer) reqMap.get("size");
	    	page.setSize(s);
	    }
	    if(reqMap.get("sort")!=null) {
	    	Sort sort = new Sort();
	    	page.setSort(sort);
	    	sort.setOrders(new ArrayList<Order>());
	    	
	    	String sortStr = (String) reqMap.get("sort");
	    	if(sortStr!=null  && sortStr.trim().length()>0) {
	    		if(sortStr.contains(",")) {
	    			String[] ss = sortStr.split(",");
	    			Order orderTmp = null;
	    			for(String s : ss) {
	    				if(s.toLowerCase().contains(" asc") ) {
	    					orderTmp = new Order();
	    					int ascInd = s.toLowerCase().indexOf(" asc");
	    					String fieldName = s.substring(0,ascInd);
	    					if(fieldName.trim().startsWith("gbkconvert$")) {
	    						orderTmp.setGbkconvert(true);
	    						fieldName=fieldName.replace("gbkconvert$", "");
	    					}
	    					String colName = FieldNameConvertor.fieldName2ColumnName(fieldName) ;
	    					
	    					orderTmp.setColName(colName);
	    					orderTmp.setSortType("asc");
	    					
	    				}else if( s.toLowerCase().contains(" desc")) {
	    					orderTmp = new Order();
	    					int ascInd = s.toLowerCase().indexOf(" desc");
	    					String fieldName = s.substring(0,ascInd);
	    					if(fieldName.trim().startsWith("gbkconvert$")) {
	    						orderTmp.setGbkconvert(true);
	    						fieldName=fieldName.replace("gbkconvert$", "");
	    					}
	    					String colName = FieldNameConvertor.fieldName2ColumnName(fieldName) ;
	    			
	    					orderTmp.setColName(colName);
	    					orderTmp.setSortType("desc");
	    				}else {
	    					
	    					orderTmp = new Order();
	    					String fieldName = s.trim();
	    					if(fieldName.trim().startsWith("gbkconvert$")) {
	    						orderTmp.setGbkconvert(true);
	    						fieldName=fieldName.replace("gbkconvert$", "");
	    					}
	    					String colName = FieldNameConvertor.fieldName2ColumnName(fieldName) ;
	    					
	    					orderTmp.setColName(colName);
	    					orderTmp.setSortType("asc");
	    					
	    				}
	    				
	    				sort.getOrders().add(orderTmp);
	    			}
	    		}else {//一列 
	    			String tmp = sortStr.trim();
	    			String[] ss = tmp.split(" ");
	    			
	    			if(ss.length==2) {//指定了一个列 和 升降序
	    				
	    				if("asc".equals(ss[1].toLowerCase().trim())  || "desc".equals(ss[1].toLowerCase().trim()) ) {
	    					Order ord = new Order();
	    					String fieldName = ss[0].trim();
	    					if(fieldName.trim().startsWith("gbkconvert$")) {
	    						ord.setGbkconvert(true);
	    						fieldName=fieldName.replace("gbkconvert$", "");
	    					}
	    				    String colName = FieldNameConvertor.fieldName2ColumnName(fieldName) ;
	    	    			
	    	    			ord.setColName(colName);
	    	    			ord.setSortType(ss[1].toLowerCase());
	    					sort.getOrders().add(ord);
	    				}
	    			}else if(ss.length==1) {//指定了一个列   但是没有指定 升降序
	    				Order ord = new Order();
	    				String fieldName = ss[0].trim();
    					if(fieldName.trim().startsWith("gbkconvert$")) {
    						ord.setGbkconvert(true);
    						fieldName=fieldName.replace("gbkconvert$", "");
    					}
	    				String colName = FieldNameConvertor.fieldName2ColumnName(fieldName) ;
		    			
		    			ord.setColName(colName);
		    			ord.setSortType("asc");
						sort.getOrders().add(ord);
	    				
	    			}
	    			
	    		}
	    		
	    	}
	    	
	    }
	  
		return page;
	}

	

}
