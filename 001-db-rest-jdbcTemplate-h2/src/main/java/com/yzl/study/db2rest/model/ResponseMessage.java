package com.yzl.study.db2rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
public final class  ResponseMessage<T> {

	private static final String FAIL = "FAIL";
	private static final String FAIL_MSG = "失败";

	private static final String SUCCESS = "SUCCESS";
	private static final String SUCCESS_MSG = "成功";

	@JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
	private T data = null;

	private String msg = null;

	private String status = FAIL;

	private long timeStamp;

	@SuppressWarnings("rawtypes")
	public static ResponseMessage failMsg() {
		return new ResponseMessage(FAIL, FAIL_MSG);
	}

	@SuppressWarnings("rawtypes")
	public static ResponseMessage failMsg(String message) {
		return new ResponseMessage(FAIL, message);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseMessage successMsg(Object data) {
		return new ResponseMessage(SUCCESS, SUCCESS_MSG, data);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseMessage successMsg(String message, Object data) {
		return new ResponseMessage(SUCCESS, message, data);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ResponseMessage successMsg() {
		return new ResponseMessage(SUCCESS, SUCCESS_MSG, null);
	}
	
	private ResponseMessage() {
	}

	private ResponseMessage(String status, String message) {
		this(status, message, null);
	}

	private ResponseMessage(String status, String message, T data) {
		this.status = status;
		this.msg = message;
		this.data = data;
		this.timeStamp = System.currentTimeMillis();

	}

	public T getData() {
		return data;
	}

	public String getMsg() {
		return msg;
	}

	public String getStatus() {
		return status;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setMsg(String message) {
		this.msg = message;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

}
