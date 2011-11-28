package com.hulefei.crawldata.manmankan.update;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

import com.hulefei.crawldata.file.FileUtil;
import com.hulefei.crawldata.manmankan.process.Constants;
import com.hulefei.crawldata.parser.NodeListIndexOutOfBoundException;
import com.hulefei.crawldata.parser.NodeListNotSingleNodeException;
import com.hulefei.crawldata.parser.ParserExp;
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
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (NodeListIndexOutOfBoundException e) {
			e.printStackTrace();
		} catch (NodeListNotSingleNodeException e) {
			e.printStackTrace();
		}
	}

	public void downloadFile() throws SQLException, IOException,
			ParserException, NodeListIndexOutOfBoundException,
			NodeListNotSingleNodeException {
		String sql = "SELECT id, url, title FROM mmk_huoying_url where tag = 0 order by rorder";
		String[][] pool = db.queryFromPool(sql);

		for (String[] strings : pool) {
			String downloadfile = FileUtil.downloadfile(strings[1], pagePath,
					strings[0] + ".html");
			String sql1 = "update mmk_huoying_url set tag = 1 where id = "
					+ strings[0];
			db.executeUpdate(sql1);
			System.out.println(downloadfile + " -finished");

			File f = new File(downloadfile);
			String[] imageNames = getImageName(f);

			for (int j = 0; j < imageNames.length; j++) {
				String imageName = imageNames[j];
				String imgurl = "http://86.manmankan.com" + imageName;
				String sql2 = "INSERT INTO mmk_huoying_imageinfo (image, rid, rorder) values ('"
						+ imgurl + "', " + strings[0] + ", " + j + ")";

				System.out.println(sql2);
				db.executeUpdate(sql2);
			}
			
			String[][] maxifnoArray = db.queryFromPool("SELECT max(id) FROM mmk_huoying_info");
			int maxInfo = Integer.valueOf(maxifnoArray[0][0]) + 1;
			String sql3 = "insert into mmk_huoying_info (id, title, rid) values ("+maxInfo+", '"+strings[2]+"',"+strings[0]+")";
			System.out.println(sql3);
			db.executeUpdate(sql3);
		}
		
		
	}

	public String[] getImageName(File f) throws ParserException,
			NodeListIndexOutOfBoundException, NodeListNotSingleNodeException,
			IOException {
		String resource = FileUtil.getResource(f, FileUtil.GB2312);
		Parser parser = new Parser(resource);
		ParserExp parserexp = new ParserExp(parser);
		Node node = parserexp.getNode("script:0");
		String script = node.toHtml();
		String imgString = parserScript(script);
		String[] split = imgString.split("\",\"");

		return split;
	}

	public String parserScript(String script) {

		int start = script.indexOf("(\"");
		int end = script.indexOf("\")");

		String substring = script.substring(start + 2, end);
		return substring;
	}

}
