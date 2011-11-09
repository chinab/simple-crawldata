package com.hulefei.crawldata.util;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface DBManager {
	
	public Connection getConnection();
	
	public Statement getStatement();
	
	public boolean connectDB(String dbname);
	
	public ResultSet executeQuery(String sql) throws SQLException;
	
	public String[][] queryFromPool(String sql) throws SQLException;
	
	public int executeUpdate(String sql) throws SQLException;
	
	public int getColumns(String sql) throws SQLException;
	
	public void close();
}