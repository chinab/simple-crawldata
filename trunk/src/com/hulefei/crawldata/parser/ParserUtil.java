package com.hulefei.crawldata.parser;

import java.util.Iterator;
import java.util.LinkedHashSet;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
 
public class ParserUtil {
	
//	private static Parser parser = new Parser();
//	 
//	/**
//	 * @deprecated
//	 * �����˽���
//	 * @param html
//	 * @param filterlist
//	 * @return
//	 * @throws ParserException
//	 */
//	public static Node filter(String html,LinkedHashSet<NodeFilter> filterlist) throws ParserException{
//		Node node = null;
//		parser.setInputHTML(html);
//		
//		for (NodeFilter filter : filterlist) {
//        	node = parser.extractAllNodesThatMatch(filter).elementAt(0);
//			if(node == null){
//				return null;
//			}
//			parser.setInputHTML(node.toHtml());
//		}
//		return node;
//	}
//	
//	/**
//	 * @deprecated
//	 * @param html
//	 * @param filter
//	 * @return
//	 * @throws ParserException
//	 */
//	public static NodeList filter2List(String html,NodeFilter filter) throws ParserException{
//		NodeList nodelist = null;
//		parser.setInputHTML(html);
//		nodelist = parser.extractAllNodesThatMatch(filter);
//		return nodelist;
//	}
	
	
	
	
	
//	public static String[] filterArray(String html,LinkedHashSet<NodeFilter> filterlist) throws ParserException{
//		Node node = null;
//		parser.setInputHTML(html);
//		
//		for (NodeFilter filter : filterlist) {
//        	node = parser.extractAllNodesThatMatch(filter).elementAt(0);
//			if(node == null){
//				return null;
//			}
//			parser.setInputHTML(node.toHtml());
//		}
//		//return node.toPlainTextString();
//	}
}
