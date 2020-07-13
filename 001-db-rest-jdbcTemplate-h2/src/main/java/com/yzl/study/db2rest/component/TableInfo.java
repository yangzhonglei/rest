package com.yzl.study.db2rest.component;

import java.util.ArrayList;
import java.util.List;

public class TableInfo {
	
	
	
	private String tableObjectName;
	
	private String tableName;
	
	private String tableType;
	
	
	private boolean havePk=false;
	
	
	private List<String> pkColName = new ArrayList<String>();
	
	
	private String singlePkColName;	
	
	
	private List<String> columnList = new ArrayList<String>();
	
	
	
	public List<String> getColumnList() {
		return columnList;
	}
	public String getTableObjectName() {
		return tableObjectName;
	}


	public void setTableObjectName(String tableObjectName) {
		this.tableObjectName = tableObjectName;
	}


	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public String getTableType() {
		return tableType;
	}


	public void setTableType(String tableType) {
		this.tableType = tableType;
	}


	public boolean isHavePk() {
		return havePk;
	}


	public void setHavePk(boolean havePk) {
		this.havePk = havePk;
	}


	public List<String> getPkColName() {
		return pkColName;
	}


//	public void setPkColName(List<String> pkColName) {
//		this.pkColName = pkColName;
//	}


	public String getSinglePkColName() {
		return singlePkColName;
	}


	public void setSinglePkColName(String singlePkColName) {
		this.singlePkColName = singlePkColName;
	}
	
	
	
	
	

}
