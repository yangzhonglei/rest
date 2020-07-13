package com.yzl.study.db2rest.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {
	
	
	
	public static String toJson(Object obj ) {
		
		
		ObjectMapper obm = new ObjectMapper();
		String result = null;
		try {
			result = obm.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			
			log.error(" object to json error: ",e);
		}
		
		return result;
	}
	

}
