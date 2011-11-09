package com.hulefei.crawldata.demo;

import java.util.HashSet;
import java.util.Set;

import com.hulefei.crawldata.spider.SimpleSpider;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class SpiderDemo {

	/** new
	 * @param args
	 */
	public static void main(String[] args) {
		
		//set DB Connection local server 暂时只支持服务模式
		HsqlDBUtil db = new HsqlDBUtil("org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost/", "sa", "");
		
		
		Set<String> listregset = new HashSet<String>();
		listregset.add("http://hulefei29.javaeye.com/\\u003Fpage=\\d+");
		Set<String> detailregset = new HashSet<String>();
		detailregset.add("http://hulefei29.javaeye.com/blog/\\d+");
		
		String seed = "http://hulefei29.javaeye.com/";
		
		SimpleSpider spider = new SimpleSpider(db, seed, listregset, detailregset);
		
		spider.run(true);
	}

}
