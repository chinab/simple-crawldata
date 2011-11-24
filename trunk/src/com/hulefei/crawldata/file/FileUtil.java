package com.hulefei.crawldata.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;



public class FileUtil {
	
	public static String UTF8 = "utf-8";
	public static String GB2312 = "gb2312";
//	static Logger logger = Logger.getLogger(FileUtil.class);

	public static String downloadfile(String fileurl,String path) throws IOException{
		
			long time = System.currentTimeMillis();
			String filename = "";
			int i = fileurl.lastIndexOf(".");
			if(i != -1){
				filename = time + fileurl.substring(i);
			}else{
				filename = time + "";
			}
			
			return downloadfile(fileurl, path, filename);
			
	}
	
	public static String downloadfile(String fileurl,String path,String filename) throws IOException{
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(fileurl);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();
        InputStream is = new BufferedInputStream(inputStream);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] bytes = new byte[40960];
		int nRead = -1;
		while ((nRead = is.read(bytes, 0, 40960)) > 0) {
			os.write(bytes, 0, nRead);
		}
		
		File dir = new File(path);
		if (!dir.exists()) {
			boolean b = dir.mkdir();
			if (!b) {
				throw new IOException("create dir failure");
			}
		}
		
		File storeFile = new File(path + filename);   
		FileOutputStream output = new FileOutputStream(storeFile);   
        output.write(os.toByteArray());   
        
        output.close(); 
		os.close();
		is.close();
		inputStream.close();
		
        return storeFile.getAbsolutePath();
       
	}
	
	public static String getResource(File file,String charset) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(file));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] bytes = new byte[40960];
		int nRead = -1;
		while ((nRead = is.read(bytes, 0, 40960)) > 0) {
			os.write(bytes, 0, nRead);
		}
		os.close();
		is.close();
		
		byte[] buf = os.toByteArray();
		String resource = new String(buf,charset);
		
		return resource;
	}
	
	public static String createDir(String path) throws IOException {
		File dir = new File(path);
		if (!dir.exists()) {
			boolean b = dir.mkdir();
			if (!b) {
				throw new IOException("create dir failure");
			}
		}
		
		return path;
	}
	
	public static void moveFilesInDirectory(String fromDir, String toDir) {
		File f=new File(fromDir);
		 File fileList[]=f.listFiles();
		 
		 for(int i=0;i<fileList.length ;i++) {
			 String filePath = toDir + fileList[i].getName();
			 fileList[i].renameTo(new File(filePath));
//			 logger.debug(filePath + "-finished");
		 }
	}

}
