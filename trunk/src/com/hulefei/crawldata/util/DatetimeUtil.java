package com.hulefei.crawldata.util;
import java.util.*;

public class DatetimeUtil {

        public static String getToday(String format){
                String datestr = null;
                Calendar calendar = Calendar.getInstance() ;
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DAY_OF_MONTH );
                
                
                if("yyyy-mm-dd".equals(format)){
                        datestr = year + "-" + formatNum(month) + "-" + formatNum(day);
                }else if("yyyymmdd".equals(format)){
                        datestr = year + formatNum(month) + formatNum(day);
                }else if("yy-mm-dd".equals(format)){
                        datestr = formatNum(year-year/100*100) + "-" + formatNum(month) + "-" + formatNum(day);
                }else if("yymmdd".equals(format)){
                        datestr = formatNum(year-year/100*100) + formatNum(month) + formatNum(day);
                }
                
                return datestr;
        }
        
        public static String getTime(String format){
                String datestr = null;
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY );
                int minus = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                
                if("h:m:s".equals(format)){
                        datestr = formatNum(hour) + ":" + formatNum(minus) + ":" + formatNum(second);
                }else if("hms".equals(format)){
                        datestr = formatNum(hour) + formatNum(minus) + formatNum(second);
                }
                
                return datestr;
        }
        
        public static String getNow(String format){
                Calendar calendar = Calendar.getInstance() ;
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH );
                int hour = calendar.get(Calendar.HOUR_OF_DAY );
                int minus = calendar.get(Calendar.MINUTE);
                int second = calendar.get(Calendar.SECOND);
                String datestr = null;
                
                if("yyyy-mm-dd h:m:s".equals(format)){
                        datestr = year + "-" + formatNum(month) + "-" + formatNum(day) + " " + formatNum(hour) + ":" + formatNum(minus) + ":" + formatNum(second);
                }
                
                return datestr;
        }
        
        public static String getToday(){
                return getToday("yyyy-mm-dd");
        }
        
        public static String getNow(){
                return getNow("yyyy-mm-dd h:m:s");
        }
        
        public static int diffDate(String date1,String date2){
                int num = 0;
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                String[] buf1 = date1.split("-");
                String[] buf2 = date2.split("-");
                try{
                        int year1 = Integer.parseInt(buf1[0]);
                        int month1 = Integer.parseInt(buf1[1]);
                        int day1 = Integer.parseInt(buf1[2]);
                        cal1.set(year1, month1, day1);
                        
                        int year2 = Integer.parseInt(buf2[0]);
                        int month2 = Integer.parseInt(buf2[1]);
                        int day2 = Integer.parseInt(buf2[2]);
                        cal2.set(year2, month2, day2);
                        
                        long x = cal2.getTimeInMillis()-cal1.getTimeInMillis();
                        x = x / (1000 * 60 * 60 * 24);
                        
                        num = (int)x;
                        
                }catch(NumberFormatException nfe){
                        System.out.println(nfe.toString());
                        return -1;
                }
                return num;
        }
        
        public static int diffToday(String date){
                String today = getToday();
                return diffDate(date,today);
        }
        
        private static String formatNum(int num){
                String str = null;
                
                if(num < 10){
                        str = "0" + num;
                }else{
                        str = num + "";
                }
                return str;
        }
        
        public static void main(String[] args) {
                int str = diffToday("2009-1-2");
                System.out.println(str);
        }
}