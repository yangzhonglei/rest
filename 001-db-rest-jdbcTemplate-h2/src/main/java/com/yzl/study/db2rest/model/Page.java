package com.yzl.study.db2rest.model;

public class Page {
	
	
	
	private  int page=1;
	private  int size=10;
	
	private  Sort sort;
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Sort getSort() {
		return sort;
	}
	public void setSort(Sort sort) {
		this.sort = sort;
	}
	
    
	
	
}
