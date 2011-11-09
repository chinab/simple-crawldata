package com.hulefei.crawldata.spider.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import com.hulefei.crawldata.spider.PageLink;



public class PageLinkImpl implements PageLink {
	static Logger logger = Logger.getRootLogger();
	String urlstr;
	String curpath;
	String rootpath;
	
	public PageLinkImpl(String url){
		this.urlstr = url;
	}
	
	public void setUrl(String url){
		this.setUrl(url);
	}
	
	public String getSource() throws IOException{ 
//		URL url = new URL(urlstr);
//		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//		InputStream inputStream = conn.getInputStream();
//		
		DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(urlstr);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();
		
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream is = new BufferedInputStream(inputStream);
        
        byte[] bytes = new byte[40960];
		int nRead = -1;
		while ((nRead = is.read(bytes, 0, 40960)) > 0) {
			os.write(bytes, 0, nRead);
		}
		os.close();
		is.close();
		inputStream.close();
		
		byte[] buf = os.toByteArray();
		String cont = new String(buf,"GBK");
		return cont;
	}
	
	
	
	public Set<String> getLinks() throws IOException, ParserException{
		Set<String> set = new HashSet<String>();
		
		String content = getSource();
		Parser parser = new Parser(content);
		NodeList nodelist = new NodeList();
		nodelist = parser.parse(new TagNameFilter("a"));
		
		SimpleNodeIterator iterator = nodelist.elements();
		while(iterator.hasMoreNodes()){
			String temp = iterator.nextNode().toHtml().trim();
			LinkTag linktag = new LinkTag();
			linktag.setText(temp);
			
			String linkurl = formatUrl(linktag.getLink());
			if(linkurl != null)
			set.add(linkurl);
		}
		return set;
	}
	
	
	
	public void setCurPath(String curpath) {
		this.curpath = curpath;
	}

	public void setRootPath(String rooturl) {
		this.rootpath = rooturl;
	}
	
	public String formatUrl(String url){
		if(url == null){
			return null;
		}
		
		String temp = url.toLowerCase();
		if(temp.indexOf("javascript") != -1){
			return null;
		}
		
		if(temp.indexOf("#") != -1)
			return null;
		
		if(url.startsWith("http://")){
			return url;
		}else if(url.startsWith("/")){
			return rootpath + url.substring(1);
		}else if(url.startsWith("../")){
			return rootpath + url.substring(3);
		}else {
			return curpath + url;
		}
	}
	
	public static void main(String[] args){
		PageLinkImpl pl = new PageLinkImpl("http://www.louisvuittonlouisvuitton.com/Class/Smallproducts_new_arrival01_1.html");
		pl.setRootPath("http://www.louisvuittonlouisvuitton.com/");
		pl.setCurPath("http://www.louisvuittonlouisvuitton.com/");
		
		try {
			Set<String> set = pl.getLinks();
			for (Iterator<String> iter = set.iterator(); iter.hasNext();) {
				String element = iter.next();
				System.out.println(element);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Set<String> getLinksByRegex(String[] regex) throws IOException {
		String source = getSource();
		Set<String> set = new HashSet<String>(); 
		
		for (int i = 0; i < regex.length; i++) {
			String regstr = regex[i];
			
			Pattern p = Pattern.compile(regstr);
			Matcher m = p.matcher(source);
			while(m.find()){
				String ret =m.group(0);
				if(ret != null){
					set.add(ret);
				}
			}
		}
		return set;
	}
}
