package com.hulefei.crawldata.spider;

public interface Filter {
	public static final int LIST = 1;
	public static final int DETAIL = 2;
	
	public int filter(String url);
}
