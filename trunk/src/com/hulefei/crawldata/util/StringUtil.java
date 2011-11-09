package com.hulefei.crawldata.util;
import java.io.UnsupportedEncodingException;

public class StringUtil {

        private static String INJSTR;
        private static String INJSTRA[];

    static {
        INJSTR = "exec|insert|select|delete|update|count|master|declare|drop|create";
        INJSTRA = INJSTR.split("\\|");
    }
        
    private static boolean ifSqlInj(String str)
    {
        if(str == null || str.length() == 0)
            return false;
        str = str.toLowerCase();
        for(int i = 0; i < INJSTRA.length; i++)
            if(str.indexOf(INJSTRA[i]) >= 0){
                System.out.println("³öÏÖ" + INJSTRA[i] + "×¢Èë");
                return true;
            }
        return false;
    }

    /**
     * ½«iso×ª»»×Ö·û´®Îªgbk¸ñÊ½
     * 
     * @param s         - ´«ÈëµÄÐèÒª×ª»»µÄ×Ö·û´®
     * @return  String ´«³öµÄ½á¹û×Ö·û´®
     */
    public static final String iso2gb(String s)
    {
        try
        {
            if(s != null)
            {
                byte abyte0[] = s.getBytes("iso-8859-1");
                return new String(abyte0, "GBK");
            } else{
                return "";
            }
        }
        catch(UnsupportedEncodingException unsupportedencodingexception)
        {
            return s;
        }
    }
    
    /**
     * ½«iso×ª»»×Ö·û´®Îªutf-8¸ñÊ½
     * 
     * @param s         - ´«ÈëµÄÐèÒª×ª»»µÄ×Ö·û´®
     * @return  String ´«³öµÄ½á¹û×Ö·û´®
     */
    public static final String iso2utf(String s)
    {
        try
        {
            if(s != null)
            {
                byte abyte0[] = s.getBytes("iso-8859-1");
                return new String(abyte0, "utf-8");
            } else{
                return "";
            }
        }
        catch(UnsupportedEncodingException unsupportedencodingexception)
        {
            return s;
        }
    }
    
    /**
     * ×ª»»ÄÚÈÝ¸ødb´æÖü£¬Ìæ»»ÌØÊâ×Ö·û('-->''ºÍÈ¥µô%)ºÍÅÐ¶ÏÃ»ÓÐsql×¢Èë£¨exec|insert|select|delete|update|count|master|declare|drop|create£©
     * 
     * @param strValue          ÒªÇóÅÐ¶ÏµÄ×Ö·û´®
     * @return          String  Èç¹ûÓÐsql×¢ÈëÔò·µ»Ø¿Õ×Ö·û´®£¬·ñÔò·µ»Ø×ª»¯ºóµÄ×Ö·û´®
     */
    public static String trans2db(String strValue)
    {
        if(strValue == null || strValue.trim().length() == 0)
            return "";
        String strRet = strValue.replaceAll("'", "''").replaceAll("%", "");
        if(ifSqlInj(strRet)){
            return "";
        }else
            return strRet;
    }
    
    /**
     * ×ª»»ÄÚÈÝ¸ødb´æÖü£¬Ìæ»»ÌØÊâ×Ö·û('-->''ºÍÈ¥µô%)ºÍÅÐ¶ÏÃ»ÓÐsql×¢Èë£¨exec|insert|select|delete|update|count|master|declare|drop|create£©
     * 
     * @param strValue          ÒªÇóÅÐ¶ÏµÄ×Ö·û´®
     * @param flag ¿ØÖÆÊÇ·ñ·ÀÖ¹sql×¢Èë£¬trueÎª¿ªÆô·À×¢Èë¹¦ÄÜ£¬falseÔò²»¿ªÆô£¬Ã»ÓÐ´Ë²ÎÊýÔòÎª¿ªÆô
     * @return          String  Èç¹ûÓÐsql×¢ÈëÔò·µ»Ø¿Õ×Ö·û´®£¬·ñÔò·µ»Ø×ª»¯ºóµÄ×Ö·û´®
     */
    public static String trans2db(String strValue,boolean flag)
    {
        if(strValue == null || strValue.trim().length() == 0)
            return "";
        String strRet = strValue.replaceAll("'", "''").replaceAll("%", "");
        if(flag || ifSqlInj(strRet) ){
            return "";
        }else
            return strRet;
    }

    /**
     * ½«ÇëÇó×Ö·û´®×ª»»³ÉÊý¾Ý¿â¿ÉÓÃµÄ×Ö·û´®<br/>
     * £¨strÎªnull»òlength=0»òÓÐsql×¢ÈëµÄ£¨exec|insert|select|delete|update|count|master|declare|drop|create£©£©·µ»Ø¿Õ<br/>
     * ·ñÔò½«Ìæ»»ÌØÊâ×Ö·û('×ª»»³É''ºÍÈ¥µô%)£¬×îºó·µ»Ø×ª»¯ºóµÄ×Ö·û´®<br/>
     * 
     * @param str
     * @return
     */
    public static String transReq2Db(String str)
    {
        if(str == null || str.length() == 0 || str.equalsIgnoreCase("null"))
            return "";
        else
            return trans2db(str);
    }
    
    /**
     * ½«ÇëÇó×Ö·û´®×ª»»³ÉÊý¾Ý¿â¿ÉÓÃµÄ×Ö·û´®<br/>
     * £¨strÎªnull»òlength=0»òÓÐsql×¢ÈëµÄ£¨exec|insert|select|delete|update|count|master|declare|drop|create£©£©·µ»Ø¿Õ<br/>
     * ·ñÔò½«Ìæ»»ÌØÊâ×Ö·û('×ª»»³É''ºÍÈ¥µô%)£¬×îºó·µ»Ø×ª»¯ºóµÄ×Ö·û´®<br/>
     * 
     * @param str
     * @param flag ¿ØÖÆÊÇ·ñ·ÀÖ¹sql×¢Èë£¬trueÎª¿ªÆô·À×¢Èë¹¦ÄÜ£¬falseÔò²»¿ªÆô£¬Ã»ÓÐ´Ë²ÎÊýÔòÎª¿ªÆô
     * @return
     */
    public static String transReq2Db(String str,boolean flag)
    {
        if(str == null || str.length() == 0 || str.equalsIgnoreCase("null"))
            return "";
        else
            return trans2db(str,flag);
    }
        
    public static String transReqcn2Db(String str)
    {
        return iso2gb(transReq2Db(str));
    }
    
    public static String transReqcn2Db(String str,boolean flag)
    {
        return iso2gb(transReq2Db(str,flag));
    }
    
    /**
     *  ×ª»»×Ö·û´®£¬Èç¹ûÊÇNULL»òÕß³¤¶ÈÎª0Ôò×ª»»ÎªÌæ»»×Ö·û´®£¬·ñÔòÔ­×Ö·û´®·µ»Ø£¨³£ÓÃº¯Êý£©
     * 
     * @param str               -Ô´×Ö·û´®
     * @param repStr    -Ìæ»»×Ö·û
     * @return  String  -·µ»Ø×Ö·û´®
     */
    public static final String transNullString(String str, String repStr)
    {
        if(str == null || str.length() == 0 || str.equalsIgnoreCase("null"))
            return repStr;
        else
            return str;
    }
        
    public static boolean isStringNull(String str){
         if(str == null || str.length() == 0 || str.equalsIgnoreCase("null"))
             return true;
         else
             return false;
    }
    
    /**
         * ÏÞÖÆÏÔÊ¾µÄÄÚÈÝ
         * @param src
         * @param limitLength
         * @return
         */
        public static String showMost(String src,int limitLength){
                
                if(src.getBytes().length>limitLength-3){
                        int totalLen = 0;
                        for(int i=0;i<src.length();i++){
                                int chrLen = src.substring(i,i+1).getBytes().length;
                                if(totalLen+chrLen>limitLength-3){
                                        return src.substring(0,i)+"...";
                                }else{
                                        totalLen+=chrLen;
                                }
                        }
                        return src;
                }else{
                        return src;
                }
        }
    
        public static String trans2Js(String source){
                source = source.replaceAll("\n","").replaceAll("\r","");
                return source;
        }
        
        
        public static void main(String[] args) {
                for (int i = 0; i < INJSTRA.length; i++) {
                        System.out.println(INJSTRA[i]);
                }
        }
        
        public static String utf82GBK(String s) {
        	try
            {
                if(s != null)
                {
                    byte abyte0[] = s.getBytes("BIG5");
                    return new String(abyte0, "utf-8");
                } else{
                    return "";
                }
            }
            catch(UnsupportedEncodingException unsupportedencodingexception)
            {
                return s;
            }
        }

}