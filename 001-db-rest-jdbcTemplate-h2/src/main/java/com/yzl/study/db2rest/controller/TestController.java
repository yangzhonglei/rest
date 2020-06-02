package com.yzl.study.db2rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class TestController {
	
	/** 明确指定的 /rest/employee/10003  这个会覆盖   SimpleRestController的getEntity
	 * @return
	 */
	@GetMapping(path = "/rest/employee/10003")
	public Object test() {
		
		return "hello world";
	}

}
