package com.hulefei.crawldata.util;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public abstract class DBManagerAbstract{

	public  Logger logger = Logger.getLogger(this.getClass());
	protected Connection conn;
	protected Statement stmt;
	
	
	public void close() {
		if(stmt != null){
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out.println(e.toString());
			}
			stmt = null;
		}
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				System.out.println(e.toString());
			}
			conn = null;
		}
	}


	
	/**
	 * 执行查询语句，返回ResultSet对象
	 * @param sql	查询语句
	 * @return		返回会查询结果
	 * @throws SQLException	
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		logger.debug(sql);
		return stmt.executeQuery(sql);
	}

	
	/**
	 * 执行INSERT, UPDATE, or DELETE，返回执行的行数
	 * @param sql
	 * @return	返回执行INSERT, UPDATE, or DELETE语句所影响的行数，0表示没有执行成功
	 * @throws SQLException
	 */
	public int executeUpdate(String sql) throws SQLException {
		logger.debug(sql);
		return stmt.executeUpdate(sql);
	}
	
	/**
	 * 返回查询结果列数
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public int getColumns(String sql) throws SQLException{
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsm = rs.getMetaData();
		int numCols = rsm.getColumnCount();
		
		logger.debug(sql);
		return numCols;
	}
	
	public int getColumns(ResultSet rs) throws SQLException{
		ResultSetMetaData rsm = rs.getMetaData();
		int numCols = rsm.getColumnCount();
		return numCols;
	}

	public Connection getConnection() {
		return conn;
	}

	public Statement getStatement() {
		return stmt;
	}

	public String[][] queryFromPool(ResultSet rs)throws SQLException{
		ResultSetMetaData rsm = rs.getMetaData();
		int numCols = rsm.getColumnCount();
		LinkedHashSet<String[]> lhset = new LinkedHashSet<String[]>();

		while(rs.next()){
			String[] temp = new String[numCols];
			for(int j = 0; j < temp.length; j++){
				temp[j] = rs.getString(j+1);
			}
			
			lhset.add(temp);
		}
		
		String[][] ret = new String[lhset.size()][];
		int i = 0;
		for (String[] strings : lhset) {
			ret[i++] = strings;
		}
		
		return ret;
	}
	
	
	/**
	 * 以二维数组形式返回查询结构
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public String[][] queryFromPool(String sql) throws SQLException{
		ResultSet rs = stmt.executeQuery(sql);
		ResultSetMetaData rsm = rs.getMetaData();
		int numCols = rsm.getColumnCount();
		LinkedHashSet<String[]> lhset = new LinkedHashSet<String[]>();

		while(rs.next()){
			String[] temp = new String[numCols];
			for(int j = 0; j < temp.length; j++){
				temp[j] = rs.getString(j+1);
			}
			
			lhset.add(temp);
		}
		
		String[][] ret = new String[lhset.size()][];
		int i = 0;
		for (String[] strings : lhset) {
			ret[i++] = strings;
		}
		
		logger.debug(sql);
		
		return ret;
	}

	
}