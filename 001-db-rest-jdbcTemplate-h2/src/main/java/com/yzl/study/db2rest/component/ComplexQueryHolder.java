package com.yzl.study.db2rest.component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yzl.study.db2rest.dao.ComplexQueryDao;
import com.yzl.study.db2rest.model.ComplexQuery;

@Component
public class ComplexQueryHolder {
	
	private static List<ComplexQuery> ComplexQueryCache =  new  ArrayList<>();
	
	
	@Autowired
	private ComplexQueryDao complexQueryDao ;
	
	@PostConstruct
	public void init(){
		
		List<ComplexQuery> list = complexQueryDao.selectAll();
		ComplexQueryCache.addAll(list);
	}
	
	public ComplexQuery  matchComplexQuery(String method ,String path) {
		
		for(ComplexQuery c : ComplexQueryCache) {
		   
			if(c.getMethod().equals(method) && c.getPath().equals(path)) {
				
				return c;
			}
		}
		return null;
	}

}
