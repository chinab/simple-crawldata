package com.hulefei.crawldata.manmankan.temp;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.ParserException;

import com.hulefei.crawldata.file.FileUtil;
import com.hulefei.crawldata.manmankan.core.DataEntity;
import com.hulefei.crawldata.parser.NodeListIndexOutOfBoundException;
import com.hulefei.crawldata.parser.NodeListNotSingleNodeException;
import com.hulefei.crawldata.parser.ParserExp;
import com.hulefei.crawldata.parser.SimpleParser;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class Step1 {

	static HsqlDBUtil db = new HsqlDBUtil("org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost/", "sa", "");
	public static String HUOYING_SEED = "http://www.manmankan.com/html/1/";
	public static String PATH = "";
	
	
	public static void main(String[] args) throws ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException, IOException, SQLException {
	
		String pagesDirPath = "F:/simple-crawldata/huoying/page/";
		String imagesDirPath = "F:/simple-crawldata/huoying/images/";
		
		Step1 app = new Step1();
		app.init(pagesDirPath, imagesDirPath);

		ArrayList<DataEntity> datalist = app.getPageDatalist(HUOYING_SEED,"F:/simple-crawldata/huoying/","seed.html");
		app.downloadFile();
//		app.insertImageInfoDB(datalist.size());
		
//		app.formatInfo();
		
		
//		new Thread(new DownloadImageThread(db, "order by rid desc")).start();
//		new Thread(new DownloadImageThread(db, "")).start();

//		app.moveFile();
	}
	
//	public void moveFile() throws SQLException, IOException {
//		String[][] pool = db.queryFromPool("SELECT id,rid FROM mmk_huoying_info");
//		
//		for (int i = 0; i < pool.length; i++) {
//			
//			String topath = "F:/simple-crawldata/huoying/images/" + pool[i][0] + "/";
//			FileUtil.createDir(topath);
//			String frompath = "F:/simple-crawldata/huoying/images1/" + pool[i][1] + "/";
//			
//			FileUtil.moveFilesInDirectory(frompath, topath);
//		}
//	}
	
	
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
	
	public void insertImageInfoDB(int size) throws SQLException, ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException, IOException {
		db.executeUpdate("DROP TABLE IF EXISTS mmk_huoying_imageinfo");
		db.executeUpdate("CREATE TABLE mmk_huoying_imageinfo (image varchar(500) not null,rid int default 0 not null, rorder int default 0 not null,tag int default 0 not null)");
		
		for (int i = 0; i < size; i++) {
			File f = new File("F:/simple-crawldata/huoying/page/" + i + ".html");
			String[] imageNames = getImageName(f);
			
			for (int j = 0; j < imageNames.length; j++) {
				String imageName = imageNames[j];
				String imgurl = "http://86.manmankan.com" + imageName;
				String sql = "INSERT INTO mmk_huoying_imageinfo (image, rid, rorder) values ('"+imgurl+"', "+i+", "+j+")";
				
				System.out.println(sql);
				db.executeUpdate(sql);
			}
		}
	}
	
	public String[] getImageName(File f) throws ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException, IOException {
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

		String substring = script.substring(start+2, end);
		return substring;
	}
	
	
	public void downloadFile() throws SQLException, IOException {
		
		String sql = "SELECT id, url FROM mmk_huoying_url order by rorder";
		String[][] pool = db.queryFromPool(sql);
		
		for (String[] strings : pool) {
			String downloadfile = FileUtil.downloadfile(strings[1],"F:/simple-crawldata/huoying/page/",strings[0] + ".html");
			System.out.println(downloadfile+" -finished");
		}
	}
	
	public void insertURLDB(ArrayList<DataEntity> datalist) throws SQLException {
		
		db.executeUpdate("DROP TABLE IF EXISTS mmk_huoying_url");
		db.executeUpdate("CREATE TABLE mmk_huoying_url (id int identity not null primary key ,title varchar(50) not null,url varchar(500) not null , rorder int default 0 not null, unique(url))");
		
		for (int i = 0; i < datalist.size(); i++) {
			DataEntity entity = datalist.get(i);
			
			String url = entity.getUrl();
			String reg = "http://manhua.manmankan.com/html/1/(\\d+).asp";
			Pattern p = Pattern.compile(reg); 
			Matcher m = p.matcher(url);
			String val = "0";
			while (m.find()){  
		      val = m.group(1);  
		    } 
			
			String sql = "INSERT INTO mmk_huoying_url (id, title, url, rorder) values ("+i+", '"+entity.getTitle()+"','"+entity.getUrl()+"', "+val+")";
			db.executeUpdate(sql);
		}
		
		System.out.println("insertDB finshed");
	}
	
	/**
	 * 创建page 和 image 目录
	 * @param pageDirPath
	 * @param imagesDirPath
	 * @throws IOException
	 */
	public void init(String pageDirPath, String imagesDirPath) throws IOException {
		File pageDir = new File(pageDirPath);//"F:/simple-crawldata/huoying/page/"
		File imagesDir = new File(imagesDirPath);//"F:/simple-crawldata/huoying/images/"
		if (!pageDir.exists()) {
			boolean b = pageDir.mkdir();
			if (!b) {
				throw new IOException("create pageDir failure");
			}
		}
		
		if (!imagesDir.exists()) {
			boolean b = imagesDir.mkdir();
			if (!b) {
				throw new IOException("create imagesDir failure");
			}
		}
	}
	
//	public void clearAll() throws SQLException {
//		
//		db.executeUpdate("DROP TABLE IF EXISTS mmk_huoying_imageinfo");
//		db.executeUpdate("DROP TABLE IF EXISTS mmk_huoying_url");
//		
//	}
	
	/**
	 * 下载页面文件
	 * @param seed 开始下载的入口
	 * @param dirpath 存储的目录
	 * @param filename seed的文件名
	 * @return
	 * @throws IOException
	 * @throws ParserException
	 * @throws NodeListIndexOutOfBoundException
	 * @throws NodeListNotSingleNodeException
	 */
	public ArrayList<DataEntity> getPageDatalist(String seed, String dirpath, String filename) throws IOException, ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException {
		//下载seed
		String path = FileUtil.downloadfile(seed, dirpath ,filename);
		System.out.println(path);
		
		//解析
//		TODO
		File f = new File(path);
		SimpleParser parser = new SimpleParser(f, SimpleParser.GB2312); 
		
		Node[] nodes1 = parser.parserForNodes(".listbg1");
		Node[] nodes2 = parser.parserForNodes(".listbg2");
		
		ArrayList<DataEntity> datalist = new ArrayList<DataEntity>();
		for (Node node : nodes1) {
			LinkTag link = getLink(node);
			if(link != null) {
				DataEntity data = new DataEntity();
				data.setTitle(link.getLinkText());
				data.setUrl(link.getLink());
				datalist.add(data);
			}
		}
		
		for (Node node : nodes2) {
			LinkTag link = getLink(node);
			if(link != null) {
				DataEntity data = new DataEntity();
				data.setTitle(link.getLinkText());
				data.setUrl(link.getLink());
				datalist.add(data);
			}
		}
		
		System.out.println("======================getDatalist finshed====================");
		
		return datalist; 
	}
	
	public LinkTag getLink(Node node) throws ParserException, NodeListIndexOutOfBoundException, NodeListNotSingleNodeException {
		Parser innerparser = new Parser(node.toHtml());
		ParserExp parserexp = new ParserExp(innerparser);
		Node[] nodes = parserexp.getNodes("a");
		if (nodes.length > 0) {
			return (LinkTag)nodes[0];
		}
		
		return null;
	}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
}
