package com.hulefei.crawldata.demo;

import java.io.IOException;

import com.hulefei.crawldata.file.FileUtil;

public class FileDemo {

	public static void main(String[] args) {   
		
		try {
			FileUtil.downloadfile("http://hulefei29.javaeye.com/blog/594222","F:/tmp/","1.html");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }   
}
