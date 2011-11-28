package com.hulefei.crawldata.manmankan.core;

import java.io.IOException;
import java.sql.SQLException;

import com.hulefei.crawldata.file.FileUtil;
import com.hulefei.crawldata.manmankan.process.Constants;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class DownloadImageThread implements Runnable {

	public Object obj = new Object();
	public int startid;
	public int endid;
	public HsqlDBUtil db;
	public String orderStr;
	public String imageDirPath;

	public DownloadImageThread(HsqlDBUtil db, String orderStr, String imageDirPath) {
		this.db = db;
		this.orderStr = orderStr;
		this.imageDirPath = imageDirPath;
	}

	@Override
	public void run() {

		while (true) {

			String[][] imageinfo = new String[0][0];
			String sql = "SELECT image, rid, rorder FROM mmk_huoying_imageinfo where tag = 0 "
					+ orderStr + " limit 1";
			try {
				imageinfo = db.queryFromPool(sql);
				if (imageinfo.length == 0) {
					System.err.println("length is 0, download images end");
					return;
				}

			} catch (SQLException e) {
				System.err.println(e.getMessage());
				System.err.println(sql);
			}

			for (int j = 0; j < imageinfo.length; j++) {
				String imgageurl = imageinfo[j][0];
				String imageName = createImageName(imgageurl,
						Integer.valueOf(imageinfo[j][2]));
				try {
					Integer rid = Integer.valueOf(imageinfo[0][1]);
					String sql1 = "SELECT id,title FROM mmk_huoying_info where rid = "
							+ rid;
					String[][] info = db.queryFromPool(sql1);
					int id = Integer.valueOf(info[0][0]);
					String title = info[0][1].trim();

					String imageForld = id + "_" + title;

					String downloadfile = FileUtil.downloadfile(imgageurl,
							imageDirPath + imageForld + "/",
							imageName);

					System.out.println(Thread.currentThread().getName()
							+ downloadfile + "- finished");

					String updatesql = "update mmk_huoying_imageinfo set tag = 1 where rid = "
							+ imageinfo[j][1]
							+ " and "
							+ "rorder = "
							+ imageinfo[j][2];

					db.executeUpdate(updatesql);

				} catch (IOException e) {
					System.out.println(e.getMessage());
					System.out.println(imgageurl);
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

	public String createImageName(String imageurl, int order) {
		int lastIndexOf = imageurl.lastIndexOf(".");
		String exp = imageurl.substring(lastIndexOf);
		String name;
		if (order < 10) {
			name = "00" + order;
		} else if (order < 100) {
			name = "0" + order;
		} else {
			name = order + "";
		}
		return name + exp;
	}
}