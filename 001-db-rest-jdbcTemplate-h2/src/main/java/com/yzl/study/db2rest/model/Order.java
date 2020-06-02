package com.yzl.study.db2rest.model;

public class Order {
	
	
	
	public static final String SORT_TYPE_ASC ="asc";
	public static final String SORT_TYPE_DESC ="desc";
	
	private String ColName;
	private String sortType;
	
	
	public String getColName() {
		return ColName;
	}
	public void setColName(String colName) {
		ColName = colName;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	
	
	
	

}
