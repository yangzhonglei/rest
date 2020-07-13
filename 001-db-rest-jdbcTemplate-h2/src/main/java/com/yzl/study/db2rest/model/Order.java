package com.yzl.study.db2rest.model;

public class Order {
	
	
	
	public static final String SORT_TYPE_ASC ="asc";
	public static final String SORT_TYPE_DESC ="desc";
	
	private String ColName;
	private String sortType;
	
	private boolean gbkconvert=false;
	
	public boolean isGbkconvert() {
		return gbkconvert;
	}
	public void setGbkconvert(boolean gbkconvert) {
		this.gbkconvert = gbkconvert;
	}
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
