package com.hulefei.crawldata.parser;
@SuppressWarnings("serial")
public class NodeListNotSingleNodeException extends Exception {

	public NodeListNotSingleNodeException(){
		super();
	}
	
	public NodeListNotSingleNodeException(String message){
		super(message);
	}
	
}