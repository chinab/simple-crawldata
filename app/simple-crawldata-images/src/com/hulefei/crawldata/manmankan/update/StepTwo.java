package com.hulefei.crawldata.manmankan.update;

import java.io.IOException;
import java.sql.SQLException;

import com.hulefei.crawldata.file.FileUtil;
import com.hulefei.crawldata.scimage.Step;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class StepTwo implements Step {

	HsqlDBUtil db;
	String pagePath;
	
	public StepTwo(HsqlDBUtil db, String pagePath) {
		this.db = db;
		this.pagePath = pagePath;
	}
	
	@Override
	public void run() {

		try {
			downloadFile();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void downloadFile() throws SQLException, IOException {
		String sql = "SELECT id, url FROM mmk_huoying_url where tag = 0 order by rorder";
		String[][] pool = db.queryFromPool(sql);
		
		for (String[] strings : pool) {
//			"F:/simple-crawldata/huoying/page/"
			String downloadfile = FileUtil.downloadfile(strings[1], pagePath, strings[0] + ".html");
			String sql1 = "update mmk_huoying_url set tag = 1 where id = " + strings[0];
			db.executeUpdate(sql1);
			System.out.println(downloadfile+" -finished");
		}
	}

}
