package com.hulefei.crawldata.manmankan.export;

import java.sql.SQLException;

import com.hulefei.crawldata.util.HsqlDBUtil;

public class Mmk_huoying_url {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HsqlDBUtil db = new HsqlDBUtil("org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost/", "sa", "");
		
		try {
			String[][] pool = db.queryFromPool("SELECT id,title,url,rorder,tag FROM mmk_huoying_url");
			for (String[] strings : pool) {
				StringBuilder sb = new StringBuilder();
				sb.append(strings[0]).append(",");
				sb.append(strings[1]).append(",");
				sb.append(strings[2]).append(",");
				sb.append(strings[3]).append(",");
				sb.append(strings[4]);
				System.out.println(sb);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
