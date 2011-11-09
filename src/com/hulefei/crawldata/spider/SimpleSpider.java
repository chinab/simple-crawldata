package com.hulefei.crawldata.spider;

import java.util.Set;

import com.hulefei.crawldata.spider.impl.UrlCrawlImpl;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class SimpleSpider {

	private StringBuilder sql = new StringBuilder();
	private HsqlDBUtil db;
	private String seed;
	private Set<String> listregset;
	private Set<String> detailregset;
	
	/**
	 * 
	 * @param db hsqldb连接方式
	 * @param seed 启示页面
	 * @param listregset list 正则
	 * @param detailregset detail 正则
	 */
	
	public SimpleSpider(HsqlDBUtil db, String seed, Set<String> listregset, Set<String> detailregset) {
		
		this.db = db;
		this.seed = seed;
		this.listregset = listregset;
		this.detailregset = detailregset;
		
		//create spider url database
		sql.append("create table spider_resource(");
		sql.append("id int identity not null primary key ,");
		sql.append("pid int default 0 null,");
		sql.append("url varchar(500) not null,");
		sql.append("type int not null,"); // 1:list,2:detail
		sql.append("site varchar(50) null,");
		sql.append("siteid int default 0 not null,");
		sql.append("httpStatus int null, ");
		sql.append("ctime char(20) default 0 not null,");
		sql.append("save int default 0 not null,");
		sql.append("crawl bit default 0 not null, ");
		sql.append("parser bit default 0 not null,");
		sql.append("childparser bit default 0 not null,");
		sql.append("childsave bit default 0 not null,");
		sql.append("reqCount int default 0 not null,");
		sql.append("unique(url))");
	}
	
	/**
	 * 开始爬取
	 * @param isCreated 是否清空数据库，重新爬取
	 */
	public void run(boolean isCreated) {
		UrlCrawlImpl uci = new UrlCrawlImpl();
		uci.setDB(db);
		
		// set seed
		uci.setSeed(seed);
		// set the set of listRegex
		uci.setListregset(listregset);
		//set the set of detailRegex
		
		uci.setDetailregset(detailregset);
		
		try {
			if (isCreated) {
				db.executeUpdate("DROP TABLE IF EXISTS spider_resource");
			}
			db.executeUpdate(sql.toString());
			uci.initDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//spider url in database
		uci.run();
	}
}
