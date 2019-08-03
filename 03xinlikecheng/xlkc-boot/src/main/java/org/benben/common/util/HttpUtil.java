package org.benben.common.util;

import net.sf.json.JSONObject;
import org.benben.common.util.Service.MyX509TrustManager;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

	 private final static int CONNECT_TIMEOUT = 5000; // in milliseconds  
     private final static String DEFAULT_ENCODING = "UTF-8";  

     public static String postData(String urlStr, String data){  
         return postData(urlStr, data, null);  
     }  

     public static String postData(String urlStr, String data, String contentType){  
         BufferedReader reader = null;  
         try {  
             URL url = new URL(urlStr);  
             URLConnection conn = url.openConnection();  
             conn.setDoOutput(true);  
             conn.setConnectTimeout(CONNECT_TIMEOUT);  
             conn.setReadTimeout(CONNECT_TIMEOUT);  
             if(contentType != null)  
                 conn.setRequestProperty("content-type", contentType);  
             OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);  
             if(data == null)  
                 data = "";  
             writer.write(data);   
             writer.flush();  
             writer.close();    

             reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));  
             StringBuilder sb = new StringBuilder();  
             String line = null;  
             while ((line = reader.readLine()) != null) {  
                 sb.append(line);  
                 sb.append("\r\n");  
             }  
             return sb.toString();  
         } catch (IOException e) {  
             //logger.error("Error connecting to " + urlStr + ": " + e.getMessage());  
         } finally {  
             try {  
                 if (reader != null)  
                     reader.close();  
             } catch (IOException e) {  
             }  
         }  
         return null;  
     }  
     
     public static Map<String, String> parseXml(HttpServletRequest request)
 			throws Exception {
 		// 解析结果存储在HashMap
 		Map<String, String> map = new HashMap<String, String>();
 		InputStream inputStream = request.getInputStream();
 		// 读取输入流
 		SAXReader reader = new SAXReader();
 		Document document = reader.read(inputStream);
 		// 得到xml根元素
 		Element root = document.getRootElement();
 		// 得到根元素的所有子节点
 		@SuppressWarnings("unchecked")
 		List<Element> elementList = root.elements();

 		// 遍历所有子节点
 		for (Element e : elementList)
 			map.put(e.getName(), e.getText());

 		// 释放资源
 		inputStream.close();
 		inputStream = null;

 		return map;
 	}

    /**
     * 访问URL,获取JSON
     *
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static JSONObject httpRequest(String requestUrl,
                                         String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
                    .openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            ce.printStackTrace();
            // logger.error("Weixin server connection timed out.");
        } catch (Exception e) {
            // logger.error("https request error:{}", e);
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getRequestParam(String url) throws Exception {
        InputStream in = null;


        URL u = new URL(url);
        // import java.net.HttpURLConnection;注意引用
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Accept-Charset", "UTF-8");
        conn.setRequestProperty("contentType", "utf-8");
        conn.setDoInput(true);
        in = conn.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in,
                "UTF-8"));
        String line = "";
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String result = buffer.toString();
        JSONObject json = JSONObject.fromObject(result);
        //String nickname = json.get("nickname").toString();
        //System.out.println(nickname);
        return json;
    }
	
}
