package org.benben.common.util;

import net.sf.json.JSONObject;
import org.benben.common.util.Service.MyX509TrustManager;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.util.*;


public class WchatUtils {
	public String before = "b1fc76989bf7d081652267ac2a08e289";
	public String a = "wx2fae7deefcf3ceb3";
	public String b = "89eff59f6143974bf52782c9a48040e7";
	public String c = "1e824905d3cfbb40b016c10a6f4d18ac";
	public String d = "1e824905d3cfbb40b016c10a6f4d18ac";
	// APPID
	public static final String APP_ID = "wx29264b38531d6be5";
//	public static final String APP_ID = "wxfd83e78fc80b0ffd";
	// APPSECRET
	public static final String APP_SECRET = "953e1c27751a625c3d8b116b63457541";
//		public static final String APP_SECRET = "156aa93e947f881f0e4280d34e2b4372";
	// APPTOKEN
	public static final String ACCESS_TOKEN = "xinmi";
	
	public static final String MCH_ID = "1504314611";
	
	//public static final String API_KEY = "9Ac3hpZVd08HuEtq3ugS6eSqvF1AuJOl";
	public static final String API_KEY = "LMYY50JGHLJ5zLEud36LgE6Q3fi4Pg0P";
	// 获取token的url
	public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APP_ID+"&secret="+APP_SECRET;
	// 当前项目的域名
//	public static final String HOST_NAME = "http://uc4aje.natappfree.cc";
	public static final String HOST_NAME = "http://xlkc.aipython.top.cc";

	public static final String PAY_CLIENT_IP = "116.255.225.149";
	// 证书路径
	public static final String MCH_CERTIFICATE_PATH = "/home/tomcat/distribution/cert/apiclient_cert.p12";
	//
//	public static String ROLL_BACK_CALLBACK = "http://uc4aje.natappfree.cc/xlkc-boot/api/v1/WX/redirectPage";
	public static String ROLL_BACK_CALLBACK = "http://xlkc.aipython.top/xlkc-boot/api/v1/WX/redirectPage";
	public static String GET_MEMBER_ROLL_BACK = HOST_NAME+"/weixin/redirectMember";
	public static String BACK_LOGIN = HOST_NAME+"/pmanage_sys/sysLogin/sysLogin.html";
	public static String FRONT_FIRST_PAGE = HOST_NAME+"/front/index.html";
	public static String ROLL_BACK_URL = HOST_NAME+"/weixin/second";
	public static String NOTIFY_URL = HOST_NAME+"/wchat/callback/before";
	
	public static Map<String,String> jkMap = new HashMap<>();
	public static String getJsapi_ticket(String token, String appid) {
		String jsapi_ticket = "";

		String key = null;
		Date d = new Date();
		if (jkMap != null && jkMap.size() > 0) {
			Set<String> set = jkMap.keySet();
			if (set != null && set.size() > 0) {
				for (String s : set) {
					key = s;
					String[] str = org.springframework.util.StringUtils.split(
							key, "_");
					if (str[1].equals(appid)) {
						double t = Long.valueOf(str[0]);
						double c = d.getTime() - t;
						if (c / 1000 / 3600 > 1.9) {
							jkMap.remove(appid);
						} else {
							return jkMap.get(key);
						}
					}
				}
			}
		}

		String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
		url = url.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = httpRequest(url, "GET", null);
		if (jsonObject.getString("errcode").equals("0")) {
			jsapi_ticket = jsonObject.getString("ticket");
			Date date = new Date();
			String mkey = date.getTime() + "_" + appid;
			jkMap.put(mkey, jsapi_ticket);
			return jsapi_ticket;
		}
		return "";
	}
	
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
			//logger.error("Weixin server connection timed out.");
		} catch (Exception e) {
			//logger.error("https request error:{}", e);
			e.printStackTrace();
		}
		return jsonObject;
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
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append(k + "=" + v + "&");
		}
		//return SHA1.encode(sb.toString().substring(0, sb.length() - 1));
		return "";
	}
	
}
