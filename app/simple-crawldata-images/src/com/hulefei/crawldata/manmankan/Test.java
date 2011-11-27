package com.hulefei.crawldata.manmankan;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hulefei.crawldata.file.FileUtil;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		String str = "http://manhua.manmankan.com/html/1/64.asp";
//		String reg = "http://manhua.manmankan.com/html/1/(\\d+).asp";
//		Pattern p = Pattern.compile(reg); 
//		Matcher m = p.matcher(str);
//		while (m.find()){  
//		      String val = m.group(1);  
//		      System.out.println("MATCH: " + val);  
//		    }  
 
//		FileUtil.downloadfile("http://86.manmankan.com/naruto/48/191.jpg","F:/tmp/","191.jpg");
		
		File f=new File("F:/simple-crawldata/huoying/images1/0/");
		 File fileList[]=f.listFiles();
		 
		 for(int i=0;i<fileList.length ;i++)
		 fileList[i].renameTo(new File("F:/simple-crawldata/huoying/images/1/" + fileList[i].getName()));
	}

}
