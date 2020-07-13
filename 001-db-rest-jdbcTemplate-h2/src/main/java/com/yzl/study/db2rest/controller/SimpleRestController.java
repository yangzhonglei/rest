package com.yzl.study.db2rest.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yzl.study.db2rest.annotation.SimpleRest;
import com.yzl.study.db2rest.component.SimpleRestRequest;
import com.yzl.study.db2rest.dao.ITableCRUD;
import com.yzl.study.db2rest.model.Order;
import com.yzl.study.db2rest.model.Page;
import com.yzl.study.db2rest.model.PageResponse;
import com.yzl.study.db2rest.model.ResponseMessage;
import com.yzl.study.db2rest.model.Sort;
import com.yzl.study.db2rest.utils.FieldNameConvertor;
import com.yzl.study.db2rest.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/edas/rest")
@Slf4j
public class SimpleRestController {
	
	@Autowired
	ITableCRUD  jdbcTemplateTableCRUD ;
	
	/**   查询 Entity   用户可以 传参数: page,size,sort进行 分页排序 <br>
	 * http://localhost:8080/rest/employees?page=1&size=10&sort=firstName,desc&sort=lastName
	 * @param req
	 * @param tableObjectName   假如表名是 t_student  这里应当传 tStudent
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping(value ="/{tableObjectName}",produces = "application/json;charset=utf-8")
	@SimpleRest
	public Object listEntity(HttpServletRequest req ,@PathVariable String tableObjectName) {
		
	    String tableName=FieldNameConvertor.fieldName2ColumnName(tableObjectName);
		SimpleRestRequest request = getPara2Request(req);
		
		log.info("===listEntity request:{}",JsonUtils.toJson(request));
		Integer count = jdbcTemplateTableCRUD.count(tableName,request);
		if(request.getPage()!=null  ) {
			
			 if( (request.getPage().getPage()-1) * request.getPage().getSize()>count) {
				 return ResponseMessage.successMsg();
			 }
		}
		
		List<Map<String, Object>> list = jdbcTemplateTableCRUD.retrieve(tableName,request);
		if(list==null) {
			list = new ArrayList<Map<String, Object>> ();
		}
		PageResponse prsb = new PageResponse();
		prsb.setList(list);
		prsb.setPage(request.getPage().getPage());
		prsb.setTotal((long)count);
		prsb.setSize(request.getPage().getSize());
		log.info("===listEntity response:{}",JsonUtils.toJson(ResponseMessage.successMsg(prsb)));
		return ResponseMessage.successMsg(prsb);
	}
	


	/**   根据id查询某一个 entity
	 * @param req
	 * @param tableObjectName  假如表名是 t_student  这里应当传 tStudent
	 * @param id     主键值
	 * @return
	 */
	@GetMapping(value="/{tableObjectName}/{id}",produces = "application/json;charset=utf-8")
	@SimpleRest
	public Object getEntity(HttpServletRequest req ,@PathVariable String tableObjectName, @PathVariable Object id) {
		
		log.info("===getEntity tableObjectName:{},id:{}",new Object[] {tableObjectName,id});
		String tableName=FieldNameConvertor.fieldName2ColumnName(tableObjectName);
		Object object = jdbcTemplateTableCRUD.retrieve(tableName,id);
		log.info("===getEntity response:{}",JsonUtils.toJson(ResponseMessage.successMsg(object)));
		return  ResponseMessage.successMsg(object);
	}
	
	
	/** 创建一条数据库记录 
	 * @param req
	 * @param tableObjectName  假如表名是 t_student  这里应当传 tStudent
	 * @param reqMap           参数map
	 * @return
	 */
	@PostMapping(value="/{tableObjectName}",produces = "application/json;charset=utf-8")
	@SimpleRest
	public Object createEntity(HttpServletRequest req ,@PathVariable String tableObjectName,@RequestBody Map<String,Object> reqMap) {
		
		 ObjectMapper  objectMapper = new ObjectMapper();
		 String reqStr=null;
		try {
			reqStr = objectMapper.writeValueAsString(reqMap);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 log.info("===createEntity reqStr:{}",reqStr);
		 Map<String,Object> reqMapCol =  FieldNameConvertor.fieldName2columnName(reqMap);
		 String tableName=FieldNameConvertor.fieldName2ColumnName(tableObjectName);
		 int i = jdbcTemplateTableCRUD.create(tableName, reqMapCol);
		 if(i==1) {
			 log.info("===createEntity response:{}",JsonUtils.toJson(ResponseMessage.successMsg()));
			 return ResponseMessage.successMsg();
		 }
		 
		log.info("===createEntity response:{}",JsonUtils.toJson(ResponseMessage.failMsg()));
		return ResponseMessage.failMsg();
	}
	
	/** 根据主键更新数据库记录  
	 * @param req
	 * @param tableObjectName   假如表名是 t_student  这里应当传 tStudent
	 * @param id                主键值
	 * @param reqMap            参数map
	 * @return
	 */
	@PutMapping(value = "/{tableObjectName}/{id}",produces = "application/json;charset=utf-8")
	@SimpleRest
	public Object updateEntity(HttpServletRequest req ,@PathVariable String tableObjectName,@PathVariable Object id ,@RequestBody Map<String,Object> reqMap) {
		
		 ObjectMapper  objectMapper = new ObjectMapper();
		 String reqStr=null;
		try {
			reqStr = objectMapper.writeValueAsString(reqMap);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 log.info("===updateEntity reqStr:{}",reqStr);
		 Map<String,Object> reqMapCol =  FieldNameConvertor.fieldName2columnName(reqMap);
		 String tableName=FieldNameConvertor.fieldName2ColumnName(tableObjectName);
		 int  i = jdbcTemplateTableCRUD.update(tableName, id ,reqMapCol);
		 if(i==1) {
			 log.info("===updateEntity response:{}",JsonUtils.toJson(ResponseMessage.successMsg()));
			 return ResponseMessage.successMsg();
		 }
		log.info("===updateEntity response:{}",JsonUtils.toJson(ResponseMessage.failMsg()));
		return ResponseMessage.failMsg();
	}
	
	
	
	/** 根据 主键删除一条数据库记录
	 * @param req
	 * @param tableObjectName
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/{tableObjectName}/{id}",produces = "application/json;charset=utf-8")
	@SimpleRest
	public Object deleteEntity(HttpServletRequest req, @PathVariable String tableObjectName, @PathVariable Integer id) {
		
		log.info("===deleteEntity tableObjectName:{},id:{}",new Object[] {tableObjectName,id});
		String tableName = FieldNameConvertor.fieldName2ColumnName(tableObjectName);
		int i = jdbcTemplateTableCRUD.delete(tableName, id);
		if (i == 1) {
			
			log.info("===deleteEntity response:{}",JsonUtils.toJson(ResponseMessage.successMsg()));
			return ResponseMessage.successMsg();
		}
		
		log.info("===deleteEntity response:{}",JsonUtils.toJson(ResponseMessage.failMsg()));
		return ResponseMessage.failMsg();
	}
	
	
	/** 解析传来的 排序参数   返回盛放<coLName,sortType>的list,  若想中文安装拼音排序:  sort=gbkconvert#name,desc
	 * @param vs
	 * @return
	 */
	private Sort  getSort(String[] vs) {
		
		Sort  sort = new Sort();
		// 传的数组sort=firstName,desc&sort=lastName传过来 sort=[firstName,desc, lastName]}
		// 也可能 sort=[lastName, firstName,desc]}   //注意 分组 空格 
		String tmpColName=null;
		String orderStr = null;
		Order orderTmp ;
		for (int i = 0; i < vs.length; i++) {
			orderTmp = new Order();
			orderStr=vs[i];
			if(orderStr.contains(",")) {
				String[] ss = orderStr.split(",");	
				tmpColName=ss[0];
				if(tmpColName.trim().startsWith("gbkconvert$")) {
					orderTmp.setGbkconvert(true);
					tmpColName=tmpColName.replace("gbkconvert$", "");
				}
				tmpColName=FieldNameConvertor.fieldName2ColumnName(tmpColName);
				orderTmp.setColName(tmpColName);
				orderTmp.setSortType(ss[1]);
			}else {
				if(orderStr.trim().startsWith("gbkconvert$")) {
					orderTmp.setGbkconvert(true);
					orderStr=orderStr.replace("gbkconvert$", "");
				}
				orderTmp.setColName(FieldNameConvertor.fieldName2ColumnName(orderStr));
				orderTmp.setSortType("asc");
				
			}
			if(sort.getOrders()==null) {
				sort.setOrders(new ArrayList<Order>());
			}
			sort.getOrders().add(orderTmp);
		}
		return sort;
	}

	
	
	private SimpleRestRequest getPara2Request(HttpServletRequest req) {
		SimpleRestRequest request =  new SimpleRestRequest();
		Page pageRequest = new Page();
		Sort sort = null;
		
		Map<String,Object> conditionParaMap = new HashMap<String,Object>();
		Enumeration<String> names = req.getParameterNames();
		while(names.hasMoreElements()) {
			String s = names.nextElement();
			String[] vs = req.getParameterValues(s);
			if(vs.length>1) {//这里传数组的 只有 传sort的一种情况
				
				if("sort".equals(s.toLowerCase())) {
					sort = getSort(vs);
					pageRequest.setSort(sort);
				}
				
			}else if (vs.length == 1){
				
				if("page".equals(s)){
					
					pageRequest.setPage(Integer.valueOf(vs[0]));
				}else if("size".equals(s)) {
					pageRequest.setSize(Integer.valueOf(vs[0]));
				}else if("sort".equals(s.toLowerCase())) {
					sort = getSort(vs);
					pageRequest.setSort(sort);
				}
				
				else {
					conditionParaMap.put(s, vs[0]);
				}
			}
		}
        conditionParaMap = FieldNameConvertor.fieldName2columnName(conditionParaMap);
        request.setConditionParaMap(conditionParaMap);
        request.setPage(pageRequest);
		return request;
	}


}
