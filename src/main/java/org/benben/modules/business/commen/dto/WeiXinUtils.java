package org.benben.modules.business.commen.dto;

import net.sf.json.JSONObject;
import org.benben.common.util.Service.MyX509TrustManager;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.Map.Entry;

public class WeiXinUtils {

	public static JSONObject httpRequestNoHttps(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化

			URL url = new URL(requestUrl);
			HttpURLConnection httpUrlConn = (HttpURLConnection) url
					.openConnection();

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
			// log.error("Weixin server connection timed out.");
		} catch (Exception e) {
			// log.error("https request error:{}", e);
			e.printStackTrace();
		}
		return jsonObject;
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

	public static String getRequestXml(SortedMap<String, String> params) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<xml>");
		Set<Entry<String, String>> set = params.entrySet();
		Iterator<Entry<String, String>> it = set.iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String k = entry.getKey();
			String v = entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)
					|| "sign".equalsIgnoreCase(k)) {
				buffer.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k
						+ ">");
			} else {
				buffer.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		buffer.append("</xml>");
		return buffer.toString();
	}

	public static String postData(String urlPath, String data,
			String contentType) throws Exception {
		BufferedReader reader = null;
		URL url = new URL(urlPath);
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		if (contentType != null) {
			conn.setRequestProperty("content-type", contentType);
		}
		OutputStreamWriter writer = new OutputStreamWriter(
				conn.getOutputStream(), "UTF-8");
		if (data == null) {
			data = "";
		}
		writer.write(data);
		writer.flush();
		writer.close();

		reader = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = "";
		while ((line = reader.readLine()) != null) {
			sb.append(line);
			sb.append("\r\n");
		}
		return sb.toString();
	}

	public static Map<String, String> xmlToMap(String xmlStr)
			throws JDOMException, IOException {
		xmlStr.replace("encoding=\".*\"", "encoding=\"UTF-8\"");
		if (xmlStr == null || "".equals(xmlStr)) {
			return null;
		}
		Map<String, String> map = new HashMap<>();
		InputStream in = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
		SAXBuilder buider = new SAXBuilder();
		Document doc = buider.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if (children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}
			map.put(k, v);
		}
		in.close();
		return map;
	}

	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator it = children.iterator();
			while (it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(WeiXinUtils.getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
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
