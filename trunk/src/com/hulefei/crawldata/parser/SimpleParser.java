package com.hulefei.crawldata.parser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

import com.hulefei.crawldata.parser.node.ImageNode;

public class SimpleParser {

	public static String UTF8 = "utf-8";
	
//	private File file;
	private Parser parser;
	
	/**
	 * 通过jquery语法可以获得各个节点，确保是Tag的情况下，强制转成Tag可以使用Tag的属性和方法
	 * 表达式语法和jquery相似，唯一的区别是即是使用id，也需要使用：来标识是第几个出现的id
	 * @param file
	 * @param charset
	 * @throws IOException
	 * @throws ParserException
	 */
	public SimpleParser(File file, String charset) throws IOException, ParserException {
//		this.file = file;
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] bytes = new byte[40960];
		int nRead = -1;
		while ((nRead = is.read(bytes, 0, 40960)) > 0) {
			os.write(bytes, 0, nRead);
		}
		os.close();
		is.close();
		
		byte[] buf = os.toByteArray();
		String resource = new String(buf,charset);
		parser = new Parser(resource);
	}
	
	/**
	 * 
	 * @param expression 表达式语法和jquery相似，唯一的区别是即是使用id，也需要使用：来标识是第几个出现的id
	 * @return 返回一个node
	 * @throws ParserException
	 * @throws NodeListIndexOutOfBoundException
	 * @throws NodeListNotSingleNodeException
	 */
	public Node parserForNode(String expression) throws ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException  {
		ParserExp parserexp = new ParserExp(parser);
		Node nodearr = parserexp.getNode(expression);
		return nodearr;
	}
	
	/**
	 * 
	 * @param expression 表达式语法和jquery相似，唯一的区别是即是使用id，也需要使用：来标识是第几个出现的id
	 * @return 返回一个去掉html的字符串
	 * @throws ParserException
	 * @throws NodeListIndexOutOfBoundException
	 * @throws NodeListNotSingleNodeException
	 */
	public String parserForString(String expression) throws ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException {
		Node node = this.parserForNode(expression);
		return node.toPlainTextString();
	}
	
	/**
	 * 
	 * @param expression 表达式语法和jquery相似，唯一的区别是即是使用id，也需要使用：来标识是第几个出现的id
	 * @return 返回一个node 数组
	 * @throws ParserException
	 * @throws NodeListIndexOutOfBoundException
	 * @throws NodeListNotSingleNodeException
	 */
	public Node[] parserForNodes(String expression) throws ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException  {
		ParserExp parserexp = new ParserExp(parser);
		Node[] nodearr = parserexp.getNodes(expression);
		return nodearr;
	}
}
