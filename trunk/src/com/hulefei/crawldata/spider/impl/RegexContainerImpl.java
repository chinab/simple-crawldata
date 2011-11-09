package com.hulefei.crawldata.spider.impl;

import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

import com.hulefei.crawldata.spider.RegexContainer;

public class RegexContainerImpl implements RegexContainer {

	Set<String> regex = null;
	
	public RegexContainerImpl(Set<String> regex){
		this.regex = regex;
	}
	
	public void setRegex(Set<String> set){
		regex = set;
	}
	
	public Set<String> getRegex() {
		return regex;
	}

	public boolean isAccord(String url) {
		
		for (Iterator<String> iter = regex.iterator(); iter.hasNext();) {
			String element =  iter.next();
			if(Pattern.matches(element, url)){
				return true;
			}
		}
		return false;
	}

}
