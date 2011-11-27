package com.hulefei.crawldata.manmankan.process;

import java.sql.SQLException;

import com.hulefei.crawldata.scimage.Step;
import com.hulefei.crawldata.util.HsqlDBUtil;

/**
 * 格式化数据
 * @author Administrator
 *
 */
public class StepFour implements Step {

	HsqlDBUtil db;
	
	public StepFour(HsqlDBUtil db) {
		this.db =db;
	}
	
	@Override
	public void run() {
		try {
			formatInfo();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void formatInfo() throws SQLException {
		db.executeUpdate("DROP TABLE IF EXISTS mmk_huoying_info");
		db.executeUpdate("CREATE TABLE mmk_huoying_info (id int identity not null primary key ,title varchar(50) not null,  rid int default 0 not null)");
		String[][] pool = db.queryFromPool("SELECT id, title FROM mmk_huoying_url order by rorder ");
		
		for (int i = 0; i < pool.length; i++) {
			String sql = "insert into mmk_huoying_info (id, title, rid) values ("+i+", '"+pool[i][1]+"', "+pool[i][0]+")";
			db.executeUpdate(sql);
			System.out.println(sql);
		}
	}
}
