package com.hulefei.crawldata.util;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HsqlDBUtil extends DBManagerAbstract {
	
	
	private final String  spider_resource = ""
			+	"create table spider_resource("
			+ 	"id int identity not null primary key ," 
			+	"pid int default 0 null," 
			+	"url varchar(500) not null," 
			+ 	"type int not null,"
			+ 	"site varchar(50) null,"
			+ 	"siteid int default 0 not null,"
			+ 	"httpStatus int null, "
			+ 	"ctime char(20) default 0 not null, "
			+ 	"save int default 0 not null,"
			+ 	"crawl bit default 0 not null, "
			+ 	"parser bit default 0 not null,"
			+ 	"childparser bit default 0 not null,"
			+ 	"childsave bit default 0 not null,"
			+ 	"reqCount int default 0 not null,"
			+ 	"unique(url)"
			+ 	")";
//	private final String spider_resource_child = ""
//			+	"create table spider_resource_child("
//			+	"id int identity not null primary key , "
//			+	"rid int default 0 not null,"
//			+	"pid int default 0 not null, "
//			+	"url varchar(500) not null, "
//			+	"type int default 0 not null," 
//			+	"site varchar(50) null,"
//			+	"siteid int default 0 not null,"
//			+	"httpStatus int null, "
//			+	"ctime char(20) default 0 not null," 
//			+	"save int default 0 not null,"
//			+	"crawl bit default 0 not null," 
//			+	"parser bit default 0 not null,"
//			+	"reqCount int default 0 not null,"
//			+	"unique(url)"
//			+	")";
//
//	private final String spider_resource_info = ""
//			+	"create table spider_resource_info("
//			+	"id int identity not null primary key , "
//			+	"title varchar(100) null,"
//			+	"cont LONGVARCHAR null,"
//			+	"srid int default 0 not null,"
//			+	"url varchar(500) not null,"
//			+	"tag varchar(50) null,"
//			+	"imgsrc varchar(500) null,"
//			+	"imgtitle varchar(100) null,"
//			+	"type int default 0 not null, "
//			+	"site varchar(50)  null,"
//			+	"siteid int default 0 not null,"
//			+	"ctime varchar(20) default 0 not null"
//			+	")";
//	private final String spider_resource_img = ""
//			+	"create table spider_resource_img("
//			+	"id int identity not null primary key , "
//			+	"rid int not null, --spider_resource_info"
//			+	"imgsrc varchar(500) null,"
//			+	"imgtitle varchar(100) null,"
//			+	"imgname varchar(100) null,"
//			+	"type int default 0 not null, "
//			+	"ctime varchar(20) default 0 not null"
//			+	")";
//	private final String spider_resource_info_child = ""
//			+	"create table spider_resource_info_child("
//			+	"id int identity not null primary key , "
//			+	"title varchar(100) null,"
//			+	"cont LONGVARCHAR null,"
//			+	"srid int default 0 not null,"
//			+	"rid int default 0 not null,"
//			+	"url varchar(500) not null,"
//			+	"imgsrc varchar(500) null,"
//			+	"imgtitle varchar(100) null,"
//			+	"type int default 0 not null, "
//			+	"site varchar(50) null,"
//			+	"siteid int default 0 not null,"
//			+	"ctime varchar(20) default 0 not null"
//			+	")";

	
	
//	/**
//	 * 使用缓存的形式链接hsqldb，推荐使用HsqlDBUtil(String path,boolean bcache)
//	 * @deprecated
//	 * @param path hsqldb的路径
//	 */
//	public HsqlDBUtil(String path){
//		try{
//			Class.forName("org.hsqldb.jdbcDriver");
//			super.conn = DriverManager.getConnection("jdbc:hsqldb:file:"+path, "sa", "");
//			super.stmt = super.conn.createStatement();
////			ResultSet rs = stmt.executeQuery("select * from spider_resource");
////			while(rs.next()){
////				String urlstr = rs.getString(3);
////				System.out.println(urlstr);
////			}
//		 }catch(Exception e){
//			 e.printStackTrace();
//		 }
//	}
	
	/**
	 * 连接hsqldb
	 * @param path	hsqldb路径
	 * @param bcache	是否使用缓存
	 */
	public HsqlDBUtil(String drivaerName, String url, String username, String password){
		try{
				Class.forName(drivaerName);
				super.conn = DriverManager.getConnection(url, username, password);
				super.stmt = super.conn.createStatement();
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
		 
		 try {
			stmt.execute(spider_resource);
//			stmt.execute(spider_resource_child);
//			stmt.execute(spider_resource_info);
//			stmt.execute(spider_resource_img);
//			stmt.execute(spider_resource_info_child);
		} catch (SQLException e) {}
		 
	}

//	public boolean connectDB(String dbname){
//		return false;
//	}
	
//	public void flush() throws SQLException{
//		super.stmt.executeUpdate("shutdown");
//	}
//	
	public void close(){
		try {
			super.stmt.executeUpdate("shutdown");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		super.close();
		
	}
}