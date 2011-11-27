package com.hulefei.crawldata.manmankan.process;

import com.hulefei.crawldata.manmankan.DownloadImageThread;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HsqlDBUtil db = new HsqlDBUtil("org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost/", "sa", "");

//		StepOne step1 = new StepOne(db);
//		step1.run();
//		StepTwo step2 = new StepTwo(db, Constants.PagesDirPath);
//		step2.run();
//		StepThree step3 = new StepThree(db);
//		step3.run();
		StepFour step4 = new StepFour(db);
		step4.run();
		
		new Thread(new DownloadImageThread(db, "order by rid desc")).start();
	}

}
