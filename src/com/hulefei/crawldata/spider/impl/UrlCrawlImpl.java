package com.hulefei.crawldata.spider.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.htmlparser.util.ParserException;

import com.hulefei.crawldata.spider.Filter;
import com.hulefei.crawldata.spider.LinkHandle;
import com.hulefei.crawldata.spider.PageLink;
import com.hulefei.crawldata.spider.RegexContainer;
import com.hulefei.crawldata.spider.UrlCrawl;
import com.hulefei.crawldata.util.DBManager;
import com.hulefei.crawldata.util.HsqlDBUtil;

/**
 * @author Jason Hu
 *
 */
public class UrlCrawlImpl implements UrlCrawl {

	public static Logger logger = Logger.getRootLogger();
	
	private Set<String> listregset = new HashSet<String>();
	private Set<String> detailregset = new HashSet<String>();
	private HsqlDBUtil db;
	private String seed;
	
	public void setSeed(String seed) {
		this.seed = seed;
	}

	public void setDB (HsqlDBUtil db){
		this.db = db;
	}
	
	public Set<String> getDetailregset() {
		return detailregset;
	}


	public void setDetailregset(Set<String> detailregset) {
		this.detailregset = detailregset;
	}


	public Set<String> getListregset() {
		return listregset;
	}
	public void setListregset(Set<String> listregset) {
		this.listregset = listregset;
	}

	public void run() {
		boolean b = false;
		while(!b){
			try {
				b = crawlUrl(db);
			} catch (IOException e) {
				logger.error(null, e);
			} catch (SQLException e) {
				logger.error(null, e);
			} catch (ParserException e) {
				logger.error(null, e);
			}
		}
	}
	
	private synchronized String getDBUrl(HsqlDBUtil db) throws SQLException{
		
		String sql = null;
		sql = "select top 1 id from spider_resource where crawl = 0 and type = 1";
			
			
		String[][] ret;
		ret = db.queryFromPool(sql);
		
		if(ret.length > 0){
			db.executeUpdate("update spider_resource set crawl = 1 where id = " + ret[0][0]);
			return ret[0][0];
		}
		return null;
	}
	
	public void initDB() throws Exception{
		
		if (seed == null) {
			throw new IllegalArgumentException("seed is null");
		}
		
		PageLink pl = new PageLinkImpl(seed);
		pl.setCurPath(getCurPath(seed));
		pl.setRootPath(getRootPath(seed));
		Set<String> linkstr = pl.getLinks();
		
		
		RegexContainer listregex = new RegexContainerImpl(listregset);
		RegexContainer detailregex = new RegexContainerImpl(detailregset);
		Filter filter = new FilterImpl(listregex,detailregex);
		
		
		System.out.println("-------------------------------");
		
		for (String url : linkstr) {
			
			logger.debug("ALL:" + url);
			
			if(filter.filter(url) == Filter.LIST){
					logger.info("LIST:" + url);
					LinkHandle llh = new ListLinkHandle(db);
					llh.handle("0",url,"1");
			} else if (filter.filter(url) == Filter.DETAIL){
					logger.info("DETAIL:" + url);
					LinkHandle dlh = new DetailLinkHandle(db);
					dlh.handle("0",url,"1"); 
			}
		}
	}
	
	
	private boolean crawlUrl(HsqlDBUtil db) throws IOException, SQLException, ParserException{
		String id = getDBUrl(db);
		if(id == null){
			logger.info("all crawled");
			return true;
		}
		String sql = null;
		sql = "select top 1 url,siteid from spider_resource where id = " + id;
		String[][] ret = db.queryFromPool(sql);
		String urlstr = "";
		if(ret.length > 0){
			urlstr = ret[0][0];
		}
		
		PageLink pl = new PageLinkImpl(urlstr);
		pl.setCurPath(getCurPath(urlstr));
		pl.setRootPath(getRootPath(urlstr));
		Set<String> linkstr = pl.getLinks();
		
		
		RegexContainer listregex = new RegexContainerImpl(listregset);
		RegexContainer detailregex = new RegexContainerImpl(detailregset);
		
		Filter filter = new FilterImpl(listregex,detailregex);
		for (String url : linkstr) {
			
			if (listregset !=null && filter.filter(url) == Filter.LIST) {
				logger.info("LIST:" + url);
				LinkHandle llh = new ListLinkHandle(db);
				llh.handle(id,url,ret[0][1]);
			} else if (detailregset !=null && filter.filter(url) == Filter.DETAIL) {
				logger.info("DETAIL:" + url);
				LinkHandle dlh = new DetailLinkHandle(db);
				dlh.handle(id,url,ret[0][1]);
			}
		}
		
		return false;
	}
	
	public String getCurPath(){
		return null;
	}
	
	private String getRootPath(String url){
		int starti = url.indexOf("http://");
		int endi = 0;
		starti = starti + 7;
		endi = url.indexOf("/", starti) + 1;
		return url.substring(0, endi);
	}
	
	private String getCurPath(String url){
		int index = url.lastIndexOf("/") + 1;
		return url.substring(0,index);
	}
	
}
