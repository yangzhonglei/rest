package com.yzl.study.db2rest.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.yzl.study.db2rest.component.ITableCRUD;

@RestController
@RequestMapping("/rest")
public class RootController {
	
	@Autowired
	ITableCRUD  tableCRUD ;
	
	@GetMapping("/{tableName}")
	public Object list(HttpServletRequest req ,@PathVariable String tableName) {
		
		 
		Enumeration<String> names = req.getParameterNames();
		Map<String,Object> para = new HashMap<String,Object>();
		
		while(names.hasMoreElements()) {
			String s = names.nextElement();
			String[] vs = req.getParameterValues(s);
			if(vs.length>1) {
				para.put(s, vs);
			}else if (vs.length == 1){
				para.put(s, vs[0]);
			}
		}
		 
		Object object = tableCRUD.retrieveAll(tableName,para);
		
		return object;
	}
	
	
	@GetMapping("/{tableName}/{id}")
	public Object invoke2(HttpServletRequest req ,@PathVariable String tableName, @PathVariable Integer id) {
		
		//String method = req.getMethod();
		
		Object object = tableCRUD.retrieve(tableName,id);
		
		return object;
	}
	
	
	@PostMapping("/{tableName}")
	public Object create(HttpServletRequest req ,@PathVariable String tableName,@RequestBody Map<String,Object> reqMap) {
		
		 System.out.println(JSONObject.toJSONString(reqMap));
		
		Object object = tableCRUD.create(tableName, reqMap);
		
		return object;
	}
	
	
	
	@PutMapping("/{tableName}/{id}")
	public Object update(HttpServletRequest req ,@PathVariable String tableName,@PathVariable Integer id ,@RequestBody Map<String,Object> reqMap) {
		
		 System.out.println(JSONObject.toJSONString(reqMap));
		
		Object object = tableCRUD.update(tableName, id ,reqMap);
		
		return object;
	}
	
	
	
	@DeleteMapping("/{tableName}/{id}")
	public Object delete(HttpServletRequest req ,@PathVariable String tableName,@PathVariable Integer id) {
		
		Object object = tableCRUD.delete(tableName, id);
		return object;
	}
	
	

}
