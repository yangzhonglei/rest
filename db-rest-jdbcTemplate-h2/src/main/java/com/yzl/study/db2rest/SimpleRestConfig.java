package com.yzl.study.db2rest;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="simple-rest-config",ignoreInvalidFields=true, ignoreUnknownFields=true)
public class SimpleRestConfig {
	
	
	private Map<String,Boolean>  permit ;
   
	private  Map<String,String[]>  exclude ;
	
	public Map<String, String[]> getExclude() {
		return exclude;
	}

	public void setExclude(Map<String, String[]> exclude) {
		this.exclude = exclude;
	}

	public Map<String, Boolean> getPermit() {
		return permit;
	}

	public void setPermit(Map<String, Boolean> permit) {
		this.permit = permit;
	}

}
