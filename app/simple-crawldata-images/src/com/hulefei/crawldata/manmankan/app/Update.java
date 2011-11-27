package com.hulefei.crawldata.manmankan.app;

import com.hulefei.crawldata.manmankan.process.Constants;
import com.hulefei.crawldata.manmankan.process.StepThree;
import com.hulefei.crawldata.manmankan.process.StepTwo;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class Update {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HsqlDBUtil db = new HsqlDBUtil("org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost/", "sa", "");
//		StepTwo step2 = new StepTwo(db, Constants.PagesDirPath);
//		step2.run();
		
		StepThree step3 = new StepThree(db);
		step3.run();
		
		
	}

}
