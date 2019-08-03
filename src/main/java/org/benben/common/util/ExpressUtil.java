package org.benben.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ExpressUtil {
	public static String express(String no)  {
		String host = "http://kdwlcxf.market.alicloudapi.com";       //【1】请求地址  支持http 和 https 及 WEBSOCKET
		String path = "/kdwlcx";                                     //【2】后缀
		String appcode = "826fe9028f174915a0f2fa4a264580b5";                             //【3】AppCode  你自己的AppCode 在买家中心查看
//		String no = "";                                     //【4】参数，快递单号
		//		String type = "YaTO";【5】参数，具体参照api接口参数
		String urlSend = host + path + "?no=" + no + "&type=" ;   //【6】拼接请求链接
		/*【1】 ~ 【6】 需要修改为对应的 可以参考产品详情 */
		String json;
		try {
			URL url = new URL(urlSend);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);//格式Authorization:APPCODE (中间是英文空格)
			int httpCode = httpURLConnection.getResponseCode();
			json = read(httpURLConnection.getInputStream());
			System.out.println("/* 获取服务器响应状态码 200 正常；400 权限错误 ； 403 次数用完； 201 快递单号错误；203快递公司不存在；204 快递公司识别失败 205 没有信息 207 单号被限制 */ ");
			System.out.println(httpCode);
			System.out.println("/* 获取返回的json */ ");
			System.out.print(json);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return json;
	}
	public static void main(String[] args) throws  Exception{
		String host = "http://kdwlcxf.market.alicloudapi.com";       //【1】请求地址  支持http 和 https 及 WEBSOCKET
		String path = "/kdwlcx";                                     //【2】后缀
		String appcode = "826fe9028f174915a0f2fa4a264580b5";                             //【3】AppCode  你自己的AppCode 在买家中心查看
		String no = "806325354023412007";                                     //【4】参数，快递单号
//		String type = "YaTO";                                            //【5】参数，具体参照api接口参数
		String urlSend = host + path + "?no=" + no + "&type=" ;   //【6】拼接请求链接
		/*【1】 ~ 【6】 需要修改为对应的 可以参考产品详情 */
		URL url = new URL(urlSend);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("Authorization", "APPCODE " + appcode);//格式Authorization:APPCODE (中间是英文空格)
		int httpCode = httpURLConnection.getResponseCode();
		String json = read(httpURLConnection.getInputStream());
		System.out.println("/* 获取服务器响应状态码 200 正常；400 权限错误 ； 403 次数用完； 201 快递单号错误；203快递公司不存在；204 快递公司识别失败 205 没有信息 207 单号被限制 */ ");
		System.out.println(httpCode);
		System.out.println("/* 获取返回的json   */ ");
		System.out.print(json);
	}
	/*
		读取返回结果
	 */
	private static String read(InputStream is) throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			line = new String(line.getBytes(), "utf-8");
			sb.append(line);
		}
		br.close();
		return sb.toString();
	}
}
