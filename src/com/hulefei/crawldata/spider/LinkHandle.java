package com.hulefei.crawldata.spider;

import java.sql.SQLException;

public interface LinkHandle {
	
	public int handle(String pid,String url,String siteid) throws SQLException;
}
