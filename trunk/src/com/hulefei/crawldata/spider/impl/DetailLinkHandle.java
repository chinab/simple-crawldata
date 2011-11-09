package com.hulefei.crawldata.spider.impl;

import java.sql.SQLException;

import com.hulefei.crawldata.spider.Filter;
import com.hulefei.crawldata.spider.LinkHandle;
import com.hulefei.crawldata.util.DatetimeUtil;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class DetailLinkHandle implements LinkHandle {

	String url = null;
	HsqlDBUtil db = null; 
	
	public DetailLinkHandle(HsqlDBUtil db){
		this.db = db;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public String getUrl(){
		return this.url;
	}
	
	public int handle(String pid, String url,String siteid) throws SQLException {
		if(!isExit(url)){
			String sql = "INSERT INTO spider_resource (pid,url,type,httpstatus,ctime,save,crawl,parser,reqcount,siteid) VALUES (" + pid + ",'" + url + "',"+Filter.DETAIL+",200,'"+DatetimeUtil.getNow()+"',0,0,0,0,'"+siteid+"')";
			return db.executeUpdate(sql);
		}else{
			return -1;
		}
	}

	
	private boolean isExit(String url) throws SQLException{
		String sql = "select * from spider_resource where url = '"+url+"'";
		String[][] ret = db.queryFromPool(sql);
		if(ret.length > 0){
			return true;
		}else{
			return false;
		}
	}
}
