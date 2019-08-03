package org.benben.common.util;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class PayCommonUtils {

	/** 
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 
     * @return boolean 
     */  
    public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {  
        StringBuffer sb = new StringBuffer();  
        Set es = packageParams.entrySet();  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
            Entry entry = (Entry)it.next();
            String k = (String)entry.getKey();  
            String v = (String)entry.getValue();  
            if(!"sign".equals(k) && null != v && !"".equals(v)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  

        sb.append("key=" + API_KEY);  

        //算出摘要  
        String mysign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();  
        String tenpaySign = ((String)packageParams.get("sign")).toLowerCase();  

        //System.out.println(tenpaySign + "    " + mysign);  
        return tenpaySign.equals(mysign);  
    }  
    
    public static String sign(String characterEncoding,SortedMap<Object,Object> parameters,String Key){
    	StringBuffer sb = new StringBuffer();  
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
            Entry entry = (Entry)it.next();
            String k = (String)entry.getKey();  
            Object v = entry.getValue();  
            if(null != v && !"".equals(v)   
                    && !"sign".equals(k) && !"key".equals(k)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  
        sb.append("key=" + Key);  
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();  
        return sign;  
    }

    /** 
     * @author 
     * @Description：sign签名 
     * @param characterEncoding 
     *            编码格式 
     * @param parameters 
     *            请求参数 
     * @return 
     */  
    public static String testSign(String characterEncoding, Map<String, Object> map, String API_KEY){
    	 StringBuffer sb = new StringBuffer();  
         Set es = map.entrySet();
         //Set<Entry<String, Object>> es = packageParams.entrySet();
         Iterator it = es.iterator();  
         while (it.hasNext()) {  
             Entry entry = (Entry) it.next();
             String k = entry.getKey().toString();  
             String v = entry.getValue().toString();  
             if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {  
                 sb.append(k + "=" + v + "&");
             }  
         }  
         sb.append("key=" + API_KEY);  
         String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();  
         //sign=MD5(sb.toString()).toUpperCase();
         return sign; 
    }
    public static String createSign(String characterEncoding, SortedMap<String,Object> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();

        Set<Entry<String, Object>> es = packageParams.entrySet();
        //Set<Entry<String, Object>> es = packageParams.entrySet();
        Iterator<Entry<String, Object>> it = es.iterator();
        for (String k : packageParams.keySet()) {
            System.out.println("key = " + k +"value = " + packageParams.get(k));
        }
        while (it.hasNext()) {  
            Entry<String, Object> entry = (Entry) it.next();
            String k = entry.getKey().toString();
            System.out.println("key: = "+ k);
            String v = entry.getValue().toString();
            System.out.println("valus : = " +v);
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {  
                sb.append(k + "=" + v + "&");
            }  
        }
        sb.append("key=" + API_KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();  
        return sign;  
    }

    public static String createSign11(String characterEncoding, SortedMap<String,Object> packageParams, String API_KEY) {
        StringBuffer sb = new StringBuffer();

        Set<Entry<String, Object>> es = packageParams.entrySet();
        //Set<Entry<String, Object>> es = packageParams.entrySet();
        Iterator<Entry<String, Object>> it = es.iterator();

        while (it.hasNext()) {
            Entry<String, Object> entry = (Entry) it.next();
            String k = entry.getKey().toString();
            System.out.println("key: = "+ k);
            String v = entry.getValue().toString();
            System.out.println("valus : = " +v);
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + API_KEY);
        System.out.println("+++++"+sb);

        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    public static String createSign1122(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {  
        StringBuffer sb = new StringBuffer();  
        Set es = packageParams.entrySet();  
        Iterator it = es.iterator();  
        while (it.hasNext()) {  
            Entry entry = (Entry) it.next();
            String k = entry.getKey().toString();  
            String v = entry.getValue().toString();  
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  
        sb.append("key=" + API_KEY);  
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();  
        return sign;  
    }

    /** 
     * @author 
     * @Description：将请求参数转换为xml格式的string 
     * @param parameters 
     *            请求参数 
     * @return 
     */  
    public static String getRequestXml(SortedMap<String,Object> parameters) {  
        StringBuffer sb = new StringBuffer();  
        sb.append("<xml>");  
        Set es = parameters.entrySet();  
        Iterator it = es.iterator();  
        while (it.hasNext()) {  
            Entry entry = (Entry) it.next();
            String k = entry.getKey().toString();  
            String v = entry.getValue().toString();   
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {  
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");  
            } else {  
                sb.append("<" + k + ">" + v + "</" + k + ">");  
            }  
        }  
        sb.append("</xml>");  
        return sb.toString();  
    }  

    /** 
     * 取出一个指定长度大小的随机正整数. 
     *  
     * @param length 
     *            int 设定所取出随机数的长度。length小于11 
     * @return int 返回生成的随机数。 
     */  
    public static int buildRandom(int length) {  
        int num = 1;  
        double random = Math.random();  
        if (random < 0.1) {  
            random = random + 0.1;  
        }  
        for (int i = 0; i < length; i++) {  
            num = num * 10;  
        }  
        return (int) ((random * num));  
    }  

    /** 
     * 获取当前时间 yyyyMMddHHmmss 
     *  
     * @return String 
     */  
    public static String getCurrTime() {  
        Date now = new Date();  
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
        String s = outFormat.format(now);  
        return s;  
    }  
    
    public static String getSignature(String noncestr, String jsapi_ticket,
			String timestamp, String url) {
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("noncestr", noncestr);
		packageParams.put("jsapi_ticket", jsapi_ticket);
		packageParams.put("timestamp", timestamp);
		packageParams.put("url", url);
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append(k + "=" + v + "&");
		}
		//return SHA1.encode(sb.toString().substring(0, sb.length() - 1));
		return "";
	}
	
}
