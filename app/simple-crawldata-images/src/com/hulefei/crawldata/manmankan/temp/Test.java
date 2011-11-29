package com.hulefei.crawldata.manmankan.temp;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hulefei.crawldata.file.FileUtil;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		HsqlDBUtil db = new HsqlDBUtil("org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost/", "sa", "");
		File dir=new File("F:/simple-crawldata/huoying/temp/images/");
		 File fileList[]=dir.listFiles();
		 
		 for(int i=0;i<fileList.length ;i++) {
			 String name = fileList[i].getName();
//			 String[][] titlePool = db.queryFromPool("select title from mmk_huoying_info where id = " + name);
//			 String title = titlePool[0][0];
			 String[] split = name.split("_");
			 
//			 String rename = name + "_" + title;
			 fileList[i].renameTo(new File("F:/simple-crawldata/huoying/temp/images/" + split[0]));
			 
		 }
//		 
	}

}
