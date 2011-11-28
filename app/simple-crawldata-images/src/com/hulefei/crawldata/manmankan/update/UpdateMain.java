package com.hulefei.crawldata.manmankan.update;

import com.hulefei.crawldata.manmankan.core.DownloadImageThread;
import com.hulefei.crawldata.manmankan.process.Constants;
import com.hulefei.crawldata.util.HsqlDBUtil;

public class UpdateMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HsqlDBUtil db = new HsqlDBUtil("org.hsqldb.jdbcDriver","jdbc:hsqldb:hsql://localhost/", "sa", "");
		StepOne stepOne = new StepOne(db);
		stepOne.run();
		
		StepTwo stepTwo = new StepTwo(db, Constants.UpdatePagesDirPath);
		stepTwo.run();
		
//		new Thread(new DownloadImageThread(db, "order by rid desc", Constants.UpdateImagesDirPath)).start();
	}

}
