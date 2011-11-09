package com.hulefei.crawldata.parser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import com.hulefei.crawldata.parser.node.ImageNode;


public class ParserExp {
	
	static Logger logger = Logger.getLogger(ParserExp.class);
	
	private Parser parser;
	
	private Set<String> tagset;
	
	public ParserExp(){
		this.tagset = getTag();
	};
	
	public ParserExp(Parser parser){
		this();
		this.parser = parser;
	}
	
	public ParserExp(String resource) throws ParserException{
		this();
		this.parser = new Parser(resource);
	}
	
	public void setParser(Parser parser){
		this.parser = parser;
	}
	
	public Node getNode(String rules) throws ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException{
		return getNode(generateRulesMap(rules));
	}
	
	public Node[] getNodes(String rules) throws ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException{
		return getNodes(generateRulesMap(rules)); 
	}
	
	
//	public NodeList getNodeList(String rules) throws ParserException, NodeListIndexOutOfBoundException{
//		return getNodeList(generateRulesMap(rules));
//	}
	
//	public Node getNode(Node node,String rules) throws NodeListIndexOutOfBoundException{
//		return getNode(node,generateRulesMap(rules));
//	}
	
//	private Node getNode(Node node,LinkedHashMap<String,String> rulesmap) throws NodeListIndexOutOfBoundException{
//		
//		String[] key = new String[rulesmap.keySet().size()];
//		rulesmap.keySet().toArray(key);
//		
//		
//		String value = null;
//		for (int i = 0; i < key.length; i++) {
//
//			if("id".equals(key[i])){
//				value = rulesmap.get(key[i]).trim();
//				int index = 0;
//				String expression = null;
//				
//				String[] valarr = value.split(":");
//				if(valarr.length == 1)
//					expression = valarr[0];
//				else if(valarr.length == 2){
//					expression = valarr[0];
//					index = Integer.parseInt(valarr[1]);
//				}
//				
//				NodeList nodelist = recursiveNode(node,new HasAttributeFilter ("id",expression));
//				if(nodelist != null && nodelist.size() > index)
//					node = nodelist.elementAt(index);
//				else
//					throw new NodeListIndexOutOfBoundException("id:"+index+"����");
//				
//			}else if("class".equals(key[i])){
//				
//				value = rulesmap.get(key[i]).trim();
//				int index = 0;
//				String expression = null;
//				
//				String[] valarr = value.split(":");
//				if(valarr.length == 1)
//					expression = valarr[0];
//				else if(valarr.length == 2){
//					expression = valarr[0];
//					index = Integer.parseInt(valarr[1]);
//				}
//				
//				NodeList nodelist = recursiveNode(node,new HasAttributeFilter ("class",expression));
//				if(nodelist != null && nodelist.size() > index)
//					node = nodelist.elementAt(index);
//				else
//					throw new NodeListIndexOutOfBoundException("not find the index which the class is " + expression + " : " + index);
//				
//			}else if(tagset.contains(key[i])){
//				
//				value = rulesmap.get(key[i]).trim();
//				int index = 0;
//				String expression = null;
//				
//				String[] valarr = value.split(":");
//				if(valarr.length == 1)
//					expression = valarr[0];
//				else if(valarr.length == 2){
//					expression = valarr[0];
//					index = Integer.parseInt(valarr[1]);
//				}
//				
//				NodeList nodelist = recursiveNode(node,new TagNameFilter (expression));
//				if(nodelist != null && nodelist.size() > index)
//					node = nodelist.elementAt(index);
//				else
//					throw new NodeListIndexOutOfBoundException(expression+":"+index+"����");
//				
//			}
//			
//		}
//		
//		return node;
//	}
	
	private Node handleNode(Node node, String value, String tag) throws ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException {
		int index = -1;
		String expression = null;
		
		String[] valarr = value.split(":");
		if(valarr.length == 1)
			expression = valarr[0];
		else if(valarr.length == 2){
			expression = valarr[0];
			index = Integer.parseInt(valarr[1]);
		}
		
		if(node == null){
			NodeList nodelist = this.parser.parse (new HasAttributeFilter (tag,expression));
			
			if(nodelist != null && nodelist.size() > index)
				if (index != -1) {
					node = nodelist.elementAt(index);
				} else {
					if (nodelist.size() == 1) {
						node = nodelist.elementAt(0);
					} else {
						throw new NodeListNotSingleNodeException();
					}
				}
			else
				throw new NodeListIndexOutOfBoundException("not find the index which the "+tag+" is " + expression + " : " + index);
		} else {
			NodeList nodelist = recursiveNode(node,new HasAttributeFilter (tag,expression));
			if(nodelist != null && nodelist.size() > index)
				if (index != -1) {
					node = nodelist.elementAt(index);
				} else {
					if (nodelist.size() == 1) {
						node = nodelist.elementAt(0);
					} else {
						throw new NodeListNotSingleNodeException();
					}
				}
			else
				throw new NodeListIndexOutOfBoundException("not find the index which the "+tag+" is " + expression + " : " + index);
		}
		
		return node;
	}
	
	private Node getNode(LinkedHashMap<String,String> rulesmap) throws ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException{
		
		String[] key = new String[rulesmap.keySet().size()];
		rulesmap.keySet().toArray(key);
		parser.reset();
		
		Node node = null;
		
		String value = null;
		for (int i = 0; i < key.length; i++) {
			
			if("id".equals(key[i])){
				value = rulesmap.get(key[i]).trim();
				node = handleNode(node, value, "id");
			}else if("class".equals(key[i])){
				value = rulesmap.get(key[i]).trim();
				node = handleNode(node, value, "class");
			}else if(tagset.contains(key[i])){
				value = rulesmap.get(key[i]).trim();
				int index = -1;
				String expression = null;
				
				String[] valarr = value.split(":");
				if(valarr.length == 1)
					expression = valarr[0];
				else if(valarr.length == 2){
					expression = valarr[0];
					index = Integer.parseInt(valarr[1]);
				}
				
				NodeList nodelist = null;
				if(node == null){
					nodelist = parser.parse (new TagNameFilter (expression));
					if(nodelist != null && nodelist.size() > index)
						if (index != -1) {
							node = nodelist.elementAt(index);
						} else {
							if (nodelist.size() == 1) {
								node = nodelist.elementAt(0);
							} else {
								throw new NodeListNotSingleNodeException();
							}
						}
					else
						throw new NodeListIndexOutOfBoundException("not find the index which the tag is " + expression + " : " + index);
				}else{
					nodelist = recursiveNode(node,new TagNameFilter (expression));
					if(nodelist != null && nodelist.size() > index)
						if (index != -1) {
							node = nodelist.elementAt(index);
						} else {
							if (nodelist.size() == 1) {
								node = nodelist.elementAt(0);
							} else {
								throw new NodeListNotSingleNodeException();
							}
						}
					else
						throw new NodeListIndexOutOfBoundException("not find the index which the tag is " + expression + " : " + index);
				}
			}
		}
		return node;
	}
	
	private Node handleNodes(Node preNode,String value, String tag) throws NodeListIndexOutOfBoundException, NodeListNotSingleNodeException, ParserException {
		
		int index = -1;
		String expression = null;
		
		String[] valarr = value.split(":");
		if(valarr.length == 1)
			expression = valarr[0];
		else if(valarr.length == 2){
			expression = valarr[0];
			index = Integer.parseInt(valarr[1]);
		}
		NodeList nodelist;
		if (preNode == null) {
			nodelist = parser.parse (new HasAttributeFilter (tag,expression));
		} else {
			nodelist = recursiveNode(preNode,new HasAttributeFilter (tag,expression));
		}
		
		if(nodelist != null){
			
			if(nodelist.size() > index) {
				if (index != -1) {
					return nodelist.elementAt(index);
				} else {
					if (nodelist.size() != 1) {
						throw new NodeListNotSingleNodeException("the "+tag+" which is " + expression + " is not single");
					}
				}
			} else
				throw new NodeListIndexOutOfBoundException("not find the index which the "+tag+" is " + expression + " : " + index);
		} 
		return null;
	}
	
	private Node[] handleLastNodes(Node preNode,String value,String tag) throws ParserException, NodeListIndexOutOfBoundException {
		int index = -1;
		String expression = null;
		
		String[] valarr = value.split(":");
		if(valarr.length == 1)
			expression = valarr[0];
		else if(valarr.length == 2){
			expression = valarr[0];
			index = Integer.parseInt(valarr[1]);
		}
		NodeList nodelist;
		if (preNode == null) {
			nodelist = parser.parse (new HasAttributeFilter (tag,expression));
			
		} else {
			nodelist = recursiveNode(preNode,new HasAttributeFilter (tag,expression));
		}
		
		if (nodelist != null) {
			if (index == -1)
				return nodelist.toNodeArray();
			else
				if (nodelist.size() > index) {
					Node node = nodelist.elementAt(index);
					Node[] nodes = new Node[1];
					nodes[0] = node;
					return nodes;
				} else {
					throw new NodeListIndexOutOfBoundException("not find the index which the id is " + expression + " : " + index); 
				}
		}
		
		return null;
	}
	
	public Node[] getNodes(LinkedHashMap<String,String> rulesmap) throws ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException{
		
		String[] key = new String[rulesmap.keySet().size()];
		rulesmap.keySet().toArray(key);
		parser.reset();
		
		
		Node node = null;
		String value = null;
		boolean islast = false;
		
//		NodeList nodelist = null;
		
		for (int i = 0; i < key.length; i++) {
			if(i == key.length-1)
				islast = true;
			
			if("id".equals(key[i])){
				value = rulesmap.get(key[i]).trim();
				
				if (islast) {
					return handleLastNodes(node, value, "id");
				} else {
					node = handleNodes(node,value,  "id");
				}
			}
			//class
			else if("class".equals(key[i])){
				value = rulesmap.get(key[i]).trim();
				
				if("class".equals(key[i])){
					value = rulesmap.get(key[i]).trim();
					
					if (islast) {
						return handleLastNodes(node, value, "class");
					} else {
						node = handleNodes(node,value,  "class");
					}
				}
			} 
			//tag
			else if(tagset.contains(key[i])) {
				value = rulesmap.get(key[i]).trim();
				int index = -1;
				String expression = null;
				
				String[] valarr = value.split(":");
				if(valarr.length == 1)
					expression = valarr[0];
				else if(valarr.length == 2){
					expression = valarr[0];
					index = Integer.parseInt(valarr[1]);
				}
				
				if(node == null){
					NodeList nodelist = parser.parse (new TagNameFilter (expression));
					if(nodelist != null){
						if(islast) {
							if(nodelist.size() > index) {
								if (index != -1) {
									node = nodelist.elementAt(index);
									Node[] nodes = new Node[1];
									nodes[0] = node; 
									return nodes;
								} else {
									return nodelist.toNodeArray();
								}
							} else
								throw new NodeListIndexOutOfBoundException("not find the index which the tag is " + expression + " : " + index);
						}
						else 
							if(nodelist.size() > index)
								node = nodelist.elementAt(index);
							else
								throw new NodeListIndexOutOfBoundException("not find the index which the tag is " + expression + " : " + index);;
					}else{
						return null;
					}
				}else{
					NodeList nodelist = recursiveNode(node,new TagNameFilter (expression));
					if(nodelist != null){
						if(islast) {
							if(nodelist.size() > index) {
								if (index != -1) {
									node = nodelist.elementAt(index);
									Node[] nodes = new Node[1];
									nodes[0] = node; 
									return nodes;
								} else {
									return nodelist.toNodeArray();
								}
							} else
								throw new NodeListIndexOutOfBoundException("not find the index which the tag is " + expression + " : " + index);
						} else 
							if(nodelist.size() > index)
								node = nodelist.elementAt(index);
							else
								throw new NodeListIndexOutOfBoundException("not find the index which the tag is " + expression + " : " + index);
					}else{
						return null;
					}
				}
			}
		}
		
		return null;
	}
	
//private NodeList getNodeList(LinkedHashMap<String,String> rulesmap) throws ParserException, NodeListIndexOutOfBoundException{
//		
//		String[] key = new String[rulesmap.keySet().size()];
//		rulesmap.keySet().toArray(key);
//		parser.reset();
//		
//		
//		Node node = null;
//		String value = null;
//		boolean islast = false;
//		
//		NodeList nodelist = null;
//		
//		for (int i = 0; i < key.length; i++) {
//			if(i == key.length-1)
//				islast = true;
//			
//			if("id".equals(key[i])){
//				value = rulesmap.get(key[i]).trim();
//				int index = 0;
//				String expression = null;
//				
//				String[] valarr = value.split(":");
//				if(valarr.length == 1)
//					expression = valarr[0].trim();
//				else if(valarr.length == 2){
//					expression = valarr[0].trim();
//					index = Integer.parseInt(valarr[1].trim());
//				}
//				
//				if(node == null){
//					nodelist = parser.parse (new HasAttributeFilter ("id",expression));
//					
//					if(nodelist != null){
//						if(islast)
//							return nodelist;
//						else 
//							if(nodelist.size() > index)
//								node = nodelist.elementAt(index);
//							else
//								throw new NodeListIndexOutOfBoundException("not find the index which the id is " + expression + " : " + index);
//					}else{
//						return null;
//					}
//					
//				}else{
//					nodelist = recursiveNode(node,new HasAttributeFilter ("id",expression));
//					
//					if(nodelist != null){
//						if(islast) {
//							return nodelist;
//						}
//						else { 
//							if(nodelist.size() > index)
//								node = nodelist.elementAt(index);
//							else
//								throw new NodeListIndexOutOfBoundException("not find the index which the id is " + expression + " : " + index);
//						}
//					}else{
//						return null;
//					}
//					
//				}
//			}else if("class".equals(key[i])){
//				value = rulesmap.get(key[i]).trim();
//				int index = 0;
//				String expression = null;
//				
//				String[] valarr = value.split(":");
//				if(valarr.length == 1)
//					expression = valarr[0];
//				else if(valarr.length == 2){
//					expression = valarr[0];
//					index = Integer.parseInt(valarr[1]);
//				}
//				
//				if(node == null){
//					nodelist = parser.parse (new HasAttributeFilter ("class",expression));
//					
//					if(nodelist != null){
//						if(islast)
//							return nodelist;
//						else 
//							if(nodelist.size() > index)
//								node = nodelist.elementAt(index);
//							else
//								throw new NodeListIndexOutOfBoundException("not find the index which the class is " + expression + " : " + index);
//					}else{
//						return null;
//					}
//				}else{
//					nodelist = recursiveNode(node,new HasAttributeFilter ("class",expression));
//					
//					if(nodelist != null){
//						if(islast)
//							return nodelist;
//						else 
//							if(nodelist.size() > index)
//								node = nodelist.elementAt(index);
//							else
//								throw new NodeListIndexOutOfBoundException("not find the index which the class is " + expression + " : " + index);
//					}else{
//						return null;
//					}
//				}
//				
//			}else if(tagset.contains(key[i])){
//				value = rulesmap.get(key[i]).trim();
//				int index = 0;
//				String expression = null;
//				
//				String[] valarr = value.split(":");
//				if(valarr.length == 1)
//					expression = valarr[0];
//				else if(valarr.length == 2){
//					expression = valarr[0];
//					index = Integer.parseInt(valarr[1]);
//				}
//				
//				
//				if(node == null){
//					nodelist = parser.parse (new TagNameFilter (expression));
//					if(nodelist != null){
//						if(islast)
//							return nodelist;
//						else 
//							if(nodelist.size() > index)
//								node = nodelist.elementAt(index);
//							else
//								throw new NodeListIndexOutOfBoundException("not find the index which the tag is " + expression + " : " + index);
//					}else{
//						return null;
//					}
//				}else{
//					nodelist = recursiveNode(node,new TagNameFilter (expression));
//					if(nodelist != null){
//						if(islast)
//							return nodelist;
//						else 
//							if(nodelist.size() > index)
//								node = nodelist.elementAt(index);
//							else
//								throw new NodeListIndexOutOfBoundException("not find the index which the tag is " + expression + " : " + index);
//					}else{
//						return null;
//					}
//				}
//			}
//		}
//		
//		return null;
//	}
	
	
	
	private ImageTag[] getImgNode(Node node){
		
		NodeList nodelist = new NodeList();
		LinkedHashSet<ImageTag> set = new LinkedHashSet<ImageTag>();
		ImageTag[] imgtagarr = null;
		node.collectInto(nodelist, new TagNameFilter("img"));
		
		SimpleNodeIterator iterator = nodelist.elements();   
		
		while(iterator.hasMoreNodes()){
			Node node1 = iterator.nextNode();
			ImageTag imgtag = new ImageTag();
			imgtag.setText(node1.toHtml());
			set.add(imgtag);
		}
		
		if(set != null)
			imgtagarr = new ImageTag[set.size()];
			
		if(imgtagarr != null)
			set.toArray(imgtagarr);
		
		return imgtagarr;
	}
	
	@Deprecated
	public Map<String,String> getImgInfo(Node node){
		ImageTag[] imgtagarr = getImgNode(node);
		Map<String,String> map = new LinkedHashMap<String,String>();
		for (int i = 0; i < imgtagarr.length; i++) {
			map.put(imgtagarr[i].getImageURL(), imgtagarr[i].getAttribute("title"));
		}
		
		return map;
	}
	
	public List<ImageNode> getImageNode(Node node) {
		ImageTag[] imgtagarr = getImgNode(node);
		List<ImageNode> list = new ArrayList<ImageNode>();
		for (int i = 0; i < imgtagarr.length; i++) {
			ImageNode imgNode = new ImageNode();
			imgNode.setImageUrl(imgtagarr[i].getImageURL());
			imgNode.setTitle(imgtagarr[i].getAttribute("title"));
			imgNode.setAlt(imgtagarr[i].getAttribute("alt"));
		}
		return list;
	}
	
	private NodeList recursiveNode(Node node, NodeFilter filter) {
		if(node == null)
				return null;
		
		NodeList nodelist = new NodeList();
		node.collectInto(nodelist, filter); 
		
		return nodelist;
	}

	private static Set<String> getTag() {
		HashSet<String> tagset = new HashSet<String>();
		tagset.add("title");
		tagset.add("body");
		tagset.add("form");
		tagset.add("input");
		tagset.add("select");
		tagset.add("img");
		tagset.add("a");
		tagset.add("ul");
		tagset.add("li");
		tagset.add("div");
		tagset.add("table");
		tagset.add("tr");
		tagset.add("td");
		tagset.add("textarea");
		tagset.add("span");
		tagset.add("style");
		tagset.add("script");
		return tagset;
	}
	
	
	private static LinkedHashMap<String, String> generateRulesMap(String rules) {
		
		LinkedHashMap<String, String> rulesmap = new LinkedHashMap<String,String>();
//		Set<String> tag = getTag();
		
		String[] temp = rules.split(">");
 		for (int i = 0; i < temp.length; i++) {
			String string = temp[i].trim();
//			int indexOf = string.indexOf(":");
//			String headstr;
//			if (indexOf != -1) {
//				headstr = string.substring(0,indexOf).toUpperCase();
//			} else {
//				headstr = string.toUpperCase();
//			}
			
			
			if(string.startsWith("#"))
				rulesmap.put("id", string.substring(1));
			else if(string.startsWith("."))
				rulesmap.put("class", string.substring(1));
			else if(string.toUpperCase().startsWith("TITLE"))
				rulesmap.put("title", string);
			else if(string.toUpperCase().startsWith("BODY"))
				rulesmap.put("body", string);
			else if(string.toUpperCase().startsWith("FORM"))
				rulesmap.put("form", string);
			else if(string.toUpperCase().startsWith("INPUT"))
				rulesmap.put("input", string);
			else if(string.toUpperCase().startsWith("SELECT"))
				rulesmap.put("select", string);
			else if(string.toUpperCase().startsWith("IMG"))
				rulesmap.put("img", string);
			else if(string.toUpperCase().startsWith("A"))
				rulesmap.put("a", string);
			else if(string.toUpperCase().startsWith("UL"))
				rulesmap.put("ul", string);
			else if(string.toUpperCase().startsWith("LI"))
				rulesmap.put("li", string);
			else if(string.toUpperCase().startsWith("DIV"))
				rulesmap.put("div", string); 
			else if(string.toUpperCase().startsWith("TABLE"))
				rulesmap.put("table", string); 
			else if(string.toUpperCase().startsWith("TR"))
				rulesmap.put("tr", string);
			else if(string.toUpperCase().startsWith("TD"))
				rulesmap.put("td", string);
			else if(string.toUpperCase().startsWith("TEXTAREA"))
				rulesmap.put("textarea", string);
			else if(string.toUpperCase().startsWith("SPAN"))
				rulesmap.put("span", string);
			else if(string.toUpperCase().startsWith("STYLE"))
				rulesmap.put("style", string);
			else if(string.toUpperCase().startsWith("SCRIPT"))
				rulesmap.put("script", string);
//			else if(string.toUpperCase().startsWith("P"))
//				rulesmap.put("p", string);
		}
		
		return rulesmap;
	}

	public static void main(String[] args) throws Exception {
		
			File f = new File("F:/tmp/1.html");
			InputStream is = new BufferedInputStream(new FileInputStream(f));
			ByteArrayOutputStream os = new ByteArrayOutputStream();
	        byte[] bytes = new byte[40960];
			int nRead = -1;
			while ((nRead = is.read(bytes, 0, 40960)) > 0) {
				os.write(bytes, 0, nRead);
			}
			os.close();
			is.close();
			
			byte[] buf = os.toByteArray();
			String resource = new String(buf,"utf-8");
			System.out.println(resource);
			Parser parser = new Parser(resource);
			ParserExp parserexp = new ParserExp(parser);
//			Node node1 = parserexp.getNode("#blog_menu:0 > li:3");
//			System.out.println(node1.toPlainTextString().trim());
			
			Node[] nodearr = parserexp.getNodes("#blog_menu:0 > a:7");
			System.out.println(nodearr.length);
			System.out.println(nodearr[0].toPlainTextString().trim());
			
			
			
		
	}
}
