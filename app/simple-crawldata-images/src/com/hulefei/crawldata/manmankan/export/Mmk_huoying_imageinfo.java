package com.hulefei.crawldata.manmankan.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import com.hulefei.crawldata.util.HsqlDBUtil;

public class Mmk_huoying_imageinfo {

	/**
	 * @param args
	 * @throws IOException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws IOException, SQLException {
		HsqlDBUtil db = new HsqlDBUtil("org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost/", "sa", "");
		File file = new File("F:/simple-crawldata/huoying/export/mmk_huoying_imageinfo.txt");
		FileOutputStream fos = new FileOutputStream(file); 
		
		
	
		String[][] pool = db.queryFromPool("SELECT image, rid, rorder,tag FROM mmk_huoying_imageinfo");
		for (String[] strings : pool) {
			StringBuilder sb = new StringBuilder();
			sb.append(strings[0]).append(",");
			sb.append(strings[1]).append(",");
			sb.append(strings[2]).append(",");
			sb.append(strings[3]).append("\r\n");
			
			fos.write(sb.toString().getBytes());
		}
		
		
		fos.close();
			
		
	}

}
