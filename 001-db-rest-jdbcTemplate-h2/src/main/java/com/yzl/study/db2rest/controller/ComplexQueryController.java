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

@RestController
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
		
		ComplexQuery c = complexQueryHolder.matchComplexQuery(method, path);
		if(c==null) {
			
			return ResponseMessage.failMsg();
		}
		
		
		Integer count = complexQueryExecuter.count(c, reqMap); 
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
			return ResponseMessage.successMsg(prsb);
			
		}
		
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
	    				if(s.toLowerCase().contains("asc") ) {
	    					
	    					int ascInd = s.toLowerCase().indexOf("asc");
	    					String fieldName = s.substring(0,ascInd);
	    					String colName = FieldNameConvertor.fieldNamed2ColumnName(fieldName) ;
	    					orderTmp = new Order();
	    					orderTmp.setColName(colName);
	    					orderTmp.setSortType("asc");
	    					
	    				}else if( s.toLowerCase().contains("desc")) {
	    					
	    					int ascInd = s.toLowerCase().indexOf("desc");
	    					String fieldName = s.substring(0,ascInd);
	    					String colName = FieldNameConvertor.fieldNamed2ColumnName(fieldName) ;
	    					orderTmp = new Order();
	    					orderTmp.setColName(colName);
	    					orderTmp.setSortType("desc");
	    				}else {
	    					String colName = FieldNameConvertor.fieldNamed2ColumnName(s.trim()) ;
	    					orderTmp = new Order();
	    					orderTmp.setColName(colName);
	    					orderTmp.setSortType("asc");
	    					
	    				}
	    				
	    				sort.getOrders().add(orderTmp);
	    			}
	    		}else {
	    			
	    			String colName = FieldNameConvertor.fieldNamed2ColumnName(sortStr.trim()) ;
	    			Order ord = new Order();
	    			ord.setColName(colName);
	    			ord.setSortType("asc");
					sort.getOrders().add(ord);
	    			
	    		}
	    		
	    	}
	    	
	    }
	  
		return page;
	}

	

}
