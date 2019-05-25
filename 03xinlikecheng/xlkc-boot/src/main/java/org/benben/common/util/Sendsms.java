package org.benben.common.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;

/**
 * 发送短信互亿无线验证码
 */
public class Sendsms {

	private static String Url = "http://api.isms.ihuyi.com/webservice/isms.php?method=Submit";

/*	public static void main(String[] args) {
		sendVerify("86 18238579787");
	}*/

	public static int sendVerify(String mobile) {
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(Url);

		//client.getParams().setContentCharset("GBK");
		client.getParams().setContentCharset("UTF-8");
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
		int mobile_code = (int)((Math.random()*9+1)*1000);

		String content = new String("Your verification code is ：" + mobile_code + "");

		NameValuePair[] data = {//提交短信
				new NameValuePair("account", "I86724405"), //查看用户名是登录用户中心->验证码短信->产品总览->APIID
				new NameValuePair("password", "a29e92e5d8069edeedcc803059758e3d"),  //查看密码请登录用户中心->验证码短信->产品总览->APIKEY
				//new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
				new NameValuePair("mobile", mobile),
				new NameValuePair("content", content),
		};
		method.setRequestBody(data);

		try {
			client.executeMethod(method);

			String SubmitResult =method.getResponseBodyAsString();

			//System.out.println(SubmitResult);

			Document doc = DocumentHelper.parseText(SubmitResult);
			Element root = doc.getRootElement();

			String code = root.elementText("code");
			String msg = root.elementText("msg");
			String smsid = root.elementText("smsid");

			System.out.println(code);
			System.out.println(msg);
			System.out.println(smsid);

			if("2".equals(code)){
				System.out.println("短信提交成功");
			}

		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			// Release connection
			method.releaseConnection();
			//			client.getConnectionManager().shutdown();
		}
		return mobile_code;
	}
}
