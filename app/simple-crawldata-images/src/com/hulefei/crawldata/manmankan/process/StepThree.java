package com.hulefei.crawldata.manmankan.process;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

import com.hulefei.crawldata.file.FileUtil;
import com.hulefei.crawldata.parser.NodeListIndexOutOfBoundException;
import com.hulefei.crawldata.parser.NodeListNotSingleNodeException;
import com.hulefei.crawldata.parser.ParserExp;
import com.hulefei.crawldata.scimage.Step;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class StepThree implements Step {

	HsqlDBUtil db;

	public StepThree(HsqlDBUtil db) {
		this.db = db;
	}

	@Override
	public void run() {
		
		try {
			String[][] fromPool = db.queryFromPool("SELECT count(*) FROM mmk_huoying_url");
			int count = Integer.valueOf(fromPool[0][0]);
			insertImageInfoDB(count);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (NodeListIndexOutOfBoundException e) {
			e.printStackTrace();
		} catch (NodeListNotSingleNodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void insertImageInfoDB(int size) throws SQLException,
			ParserException, NodeListIndexOutOfBoundException,
			NodeListNotSingleNodeException, IOException {
		db.executeUpdate("DROP TABLE IF EXISTS mmk_huoying_imageinfo");
		db.executeUpdate("CREATE TABLE mmk_huoying_imageinfo (image varchar(500) not null,rid int default 0 not null, rorder int default 0 not null,tag int default 0 not null)");

		for (int i = 0; i < size; i++) {
			File f = new File(Constants.PagesDirPath + i + ".html");
			String[] imageNames = getImageName(f);

			for (int j = 0; j < imageNames.length; j++) {
				String imageName = imageNames[j];
				String imgurl = "http://86.manmankan.com" + imageName;
				String sql = "INSERT INTO mmk_huoying_imageinfo (image, rid, rorder) values ('"
						+ imgurl + "', " + i + ", " + j + ")";

				System.out.println(sql);
				db.executeUpdate(sql);
			}
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
