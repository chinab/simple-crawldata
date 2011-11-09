package com.hulefei.crawldata.spider.impl;

import com.hulefei.crawldata.spider.Filter;
import com.hulefei.crawldata.spider.RegexContainer;

public class FilterImpl implements Filter {
	
	public RegexContainer listregex = null;
	public RegexContainer detailregex = null;

	public FilterImpl(){}
	
	public FilterImpl(RegexContainer listregex,RegexContainer detailregex){
		this.listregex = listregex;
		this.detailregex = detailregex;
	}
	
	public void setListRegex(RegexContainer listregex){
		this.listregex = listregex;
	}
	public void setDetailRegex(RegexContainer detailregex){
		this.detailregex = detailregex;
	}

	public int filter(String url) {
		
		if(listregex != null && listregex.isAccord(url)  ){
			return Filter.LIST;
		}else if( detailregex != null && detailregex.isAccord(url)){
			return Filter.DETAIL;
		}else{
			return -1;
		}
	}
}
