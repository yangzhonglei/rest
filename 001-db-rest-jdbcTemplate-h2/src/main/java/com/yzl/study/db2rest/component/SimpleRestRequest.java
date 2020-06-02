package com.yzl.study.db2rest.component;

import java.util.HashMap;
import java.util.Map;

import com.yzl.study.db2rest.model.Page;

public class SimpleRestRequest {

	
	private Map<String,Object> conditionParaMap;
	
	
	private Page page ;

    

	public void setConditionParaMap(Map<String, Object> conditionParaMap) {
		this.conditionParaMap = conditionParaMap;
	}

	public Map<String, Object> getConditionParaMap() {
		return conditionParaMap;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}


	
}
