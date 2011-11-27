package com.hulefei.crawldata.manmankan;

import java.io.IOException;
import java.sql.SQLException;

import com.hulefei.crawldata.file.FileUtil;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class DownloadImageThread implements Runnable {

	public Object obj = new Object();
	public int startid;
	public int endid;
	public HsqlDBUtil db;
	public String orderStr;

	public DownloadImageThread(HsqlDBUtil db, String orderStr) {
		this.db = db;
		this.orderStr = orderStr;
	}

	@Override
	public void run() {

//		int count = 0;
		while(true) {
//			count++;
			String[][] imageinfo = new String[0][0];
			String sql = "SELECT image, rid, rorder FROM mmk_huoying_imageinfo where tag = 0 "+orderStr+" limit 1";
			try {
					imageinfo = db.queryFromPool(sql);
					
					System.out.println(sql);
					
//					String sql1 = "SELECT id FROM mmk_huoying_info where rid = " + imageinfo[0][1];
//					String[][] realids = db.queryFromPool(sql1);
//					imageinfo[0][1] = realids[0][0];
					
					
					
				if (imageinfo.length == 0) {
					System.err.println("length is 0");
					return;
				}
			} catch (SQLException e) {
				System.err.println(e.getMessage());
				System.err.println(sql);
			}

			for (int j = 0; j < imageinfo.length; j++) {
				String imgageurl = imageinfo[j][0];
				String imageName = createImageName(imgageurl, Integer.valueOf(imageinfo[j][2]));
				try {
					String downloadfile = FileUtil.downloadfile(imgageurl,
							"F:/simple-crawldata/huoying/images/" + imageinfo[j][1] + "/",
							imageName);
					
					System.out.println(Thread.currentThread().getName() +downloadfile + "- finished");
					
					String updatesql = "update mmk_huoying_imageinfo set tag = 1 where rid = " + imageinfo[j][1] + " and " + "rorder = "+ imageinfo[j][2];
					
					
					db.executeUpdate(updatesql);
					
					
				} catch (IOException e) {
					System.out.println(e.getMessage());
					System.out.println(imgageurl);
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				} catch(Exception e) {
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