package com.hulefei.crawldata.demo;


import java.io.File;
import org.htmlparser.Node; 
import org.htmlparser.Tag;
import com.hulefei.crawldata.parser.SimpleParser;

public class ParserDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		File f = new File("F:/tmp/1.html");
		
		SimpleParser parser = new SimpleParser(f, SimpleParser.UTF8);
	
		Node node = parser.parserForNode("img:0");
		String string = node.toHtml();
		System.out.println(string);
		Tag image = (Tag)node;
		System.out.println(image.getAttribute("src"));
		System.out.println();
		//------------------------------------------------------------------
		String string2 = parser.parserForString(".next:0");
		System.out.println(string2);
		System.out.println();
		
		//-----------------------------------------------------------------------
		Node[] nodes = parser.parserForNodes(".logo");
		for (Node node2 : nodes) {
			System.out.println(node2.toHtml());
		}
		
		System.out.println();
		
		//-----------------------------------------------------------------------
		 Node node2 = parser.parserForNode("#share_weibo > a:0");
		 System.out.println(node2.toHtml());
//		Node[] nodes2 = parser.parserForNodes("#share_weibo > a:0");
//		for (Node node2 : nodes2) {
//			System.out.println(node2.toHtml());
//		}
		 
	}
}
