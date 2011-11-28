package com.hulefei.crawldata.manmankan.update;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.ParserException;

import com.hulefei.crawldata.file.FileUtil;
import com.hulefei.crawldata.manmankan.DataEntity;
import com.hulefei.crawldata.manmankan.process.Constants;
import com.hulefei.crawldata.parser.NodeListIndexOutOfBoundException;
import com.hulefei.crawldata.parser.NodeListNotSingleNodeException;
import com.hulefei.crawldata.parser.ParserExp;
import com.hulefei.crawldata.parser.SimpleParser;
import com.hulefei.crawldata.scimage.Step;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class StepOne implements Step {

HsqlDBUtil db;
	
	
	public StepOne(HsqlDBUtil db) {
		this.db = db;
	}
	
	public void run() {
		try {
			init(Constants.UpdatePagesDirPath, Constants.UpdateImagesDirPath);
			ArrayList<DataEntity> pageDatalist = getPageDatalist(Constants.HUOYING_SEED, Constants.UpdateDirPath, "seed.html");
			List<DataEntity> newURLList = getNewUrl(pageDatalist);
			insertNewURL(newURLList);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserException e) {
			e.printStackTrace();
		} catch (NodeListIndexOutOfBoundException e) {
			e.printStackTrace();
		} catch (NodeListNotSingleNodeException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	/**
	 * 获取list数据
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
	
	/**
	 * 将list数据插入数据库
	 * @param datalist
	 * @throws SQLException
	 */
	public List<DataEntity> getNewUrl(ArrayList<DataEntity> datalist) throws SQLException {
		
		List<DataEntity> al = new ArrayList<DataEntity>();
		
		for (int i = 0; i < datalist.size(); i++) {
			DataEntity entity = datalist.get(i);
			String sql = "SELECT count(*) FROM mmk_huoying_url where url = '"+entity.getUrl()+"'";
			String[][] queryFromPool = db.queryFromPool(sql);
			if (Integer.valueOf(queryFromPool[0][0]) == 0) {
				al.add(entity);
			}
		}
		
		return al;
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
	
	public void insertNewURL(List<DataEntity> datalist) throws SQLException {
		
		
		
		for (int i =0; i < datalist.size(); i++) {
			
			String[][] maxtemp = db.queryFromPool("SELECT max(id) FROM mmk_huoying_url");
			int max = Integer.valueOf(maxtemp[0][0]) + 1;
			
			DataEntity entity = datalist.get(i);
			String url = entity.getUrl();
			System.out.println("add:" + url);
			String reg = "http://manhua.manmankan.com/html/1/(\\d+).asp";
			Pattern p = Pattern.compile(reg); 
			Matcher m = p.matcher(url);
			String val = "0";
			while (m.find()){  
		      val = m.group(1);  
		    } 
			
			String sql = "INSERT INTO mmk_huoying_url (id, title, url, rorder) values ("+max+", '"+entity.getTitle()+"','"+entity.getUrl()+"', "+val+")";
			db.executeUpdate(sql);
		}
	}

}
