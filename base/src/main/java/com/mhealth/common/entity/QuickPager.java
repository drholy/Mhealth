package com.mhealth.common.entity;

import com.mhealth.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class QuickPager<T> {
	
	private int currPage = 1;// 当前第几页
	private int pageSize = 10;// 每页的记录数
	private int totalRows = 0;// 数据的总量
	private int totalPages = 0;// 页数总量
	private boolean ispage = true;//是否执行分页查询
	
	private int beginNum=0;//数据库取值的开始索引
	
	private List<T> data = new ArrayList<T>(0);

	public QuickPager(){
	}
	public QuickPager(int currpage) {
		this.currPage = currpage;
	}


	public QuickPager(Integer currPage, Integer pageSize) {
		if(currPage == null && pageSize==null ) {
			ispage = false;
		} else {
			if(currPage == null || currPage <= 0){
				currPage = 1;

			}
			this.currPage = currPage;
			if(pageSize == null || pageSize <= 0) {
				pageSize = 10;

			}
			this.pageSize = pageSize;
		}
	}

	public QuickPager(String currPage, String pageSize) {
		if(StringUtils.isEmpty(currPage)&&StringUtils.isEmpty(pageSize)){
			this.ispage=false;
		}
		if(currPage!=null){
			this.currPage=Integer.valueOf(currPage);
		}
		if(pageSize!=null){
			this.pageSize=Integer.valueOf(pageSize);
		}
	}

	/*public QuickPager(String currPage, String pageSize) {
		try {
			this.currPage = StringUtils.isEmpty(currPage) ? 1 : Integer.parseInt(currPage);
			this.pageSize = StringUtils.isEmpty(pageSize) ? 10 : Integer.parseInt(pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
	public void addData(T ele) {
		if(this.data == null ) {
			this.data = new ArrayList<T>();
		}
		this.data.add(ele);
	}

	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getMaxLine() {
		return pageSize;
	}
	public int getTotalRows() {
		return totalRows;
	}
	public void setTotalRows(int totalRows) {

		this.totalRows = totalRows;
		this.totalPages = (this.totalRows == 0 ? 0 : (this.totalRows % this.pageSize != 0 ? this.totalRows / this.pageSize + 1 : this.totalRows / this.pageSize));
		//if(this.currPage>this.totalPages+1)this.currPage=this.totalPages;//要查看的页数大于总页数
		this.beginNum=((this.getCurrPage() - 1) * this.getMaxLine());
		//System.err.println("总页数:"+this.totalPages);
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	
	/*public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}*/
	
	public String getSqlLimit() {
		if( ! this.ispage ){
			return "";
		}
		return " limit " +String.valueOf(this.beginNum ) + " , " + String.valueOf(pageSize);
	}
	

	public int getBeginNum() {
		if( ! this.ispage ){
			return 0;
		}
		return this.beginNum;
	}
	public int getEndNum(){
		if( ! this.ispage ){
			return this.totalRows;
		}
		return getMaxLine();
	}


	public void setBeginNum(int beginNum) {
		this.beginNum = beginNum;
	}


	/**
	 * 设置是否执行分页查询
	 * @param ispage
	 */
	public void setIspage(boolean ispage) {
		this.ispage = ispage;
	}

}
