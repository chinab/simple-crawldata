package com.hulefei.crawldata.spider;

import java.io.IOException;
import java.util.Set;

import org.htmlparser.util.ParserException;

public interface PageLink {
	
	public Set<String> getLinks()  throws IOException, ParserException;
	
	public void setRootPath(String rooturl);
	
	public void setCurPath(String curpath);
	
	public String formatUrl(String url);
	
	public Set<String> getLinksByRegex(String[] regex) throws IOException;
	
	public String getSource() throws IOException;
}
