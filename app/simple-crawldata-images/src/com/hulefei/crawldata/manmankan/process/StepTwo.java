package com.hulefei.crawldata.manmankan.process;

import java.io.IOException;
import java.sql.SQLException;

import com.hulefei.crawldata.file.FileUtil;
import com.hulefei.crawldata.scimage.Step;
import com.hulefei.crawldata.util.HsqlDBUtil;

/**
 * 第二步抓取的数据页面
 * @author Administrator
 *
 */
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
		String sql = "SELECT id, url FROM mmk_huoying_url order by rorder";
		String[][] pool = db.queryFromPool(sql);
		
		for (String[] strings : pool) {
//			"F:/simple-crawldata/huoying/page/"
			String downloadfile = FileUtil.downloadfile(strings[1], pagePath, strings[0] + ".html");
			System.out.println(downloadfile+" -finished");
		}
	}
	
}
