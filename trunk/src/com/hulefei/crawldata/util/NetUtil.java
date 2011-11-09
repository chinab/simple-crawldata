package com.hulefei.crawldata.util;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class NetUtil {
        
        
        public static String getSource(String urlstr) throws IOException{
                URL url = new URL(urlstr);
                URLConnection connection = url.openConnection(); 
                InputStream inputstream = connection.getInputStream();
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                InputStream is = new BufferedInputStream(inputstream);
                
                int iRead = -1;
                byte[] buf = new byte[1024];
                while((iRead = is.read(buf, 0, 1024)) > 0){
                        os.write(buf, 0, iRead);
                }
                
                is.close();
                os.close();
                inputstream.close();
                
                byte[] bytes = os.toByteArray();
                String cont = new String(bytes,"GBK");
                
                return cont;
        }
        
        public static void toHtml(String urlstr,String path) throws IOException{
                String source = getSource(urlstr);
                FileWriter fw = new FileWriter(path);
                fw.write(source);
                fw.close();
                System.out.println(urlstr + ":" + path + "--" + "Íê³É");
        }
        
        public static void toHtml(Map<String, String> map) throws IOException{
                Set<Map.Entry<String,String>> set = map.entrySet();
                FileWriter fw = null;
                for (Entry<String, String> entry : set) {
                        String source = getSource(entry.getKey());
                        fw = new FileWriter(entry.getValue());
                        fw.write(source);
                        fw.flush();
                        System.out.println(entry.getKey() + "  " + entry.getValue() + "--" + "Íê³É");
                }
                fw.close();
        }
}