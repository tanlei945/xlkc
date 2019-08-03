package org.benben.modules.business.commen.service.impl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.benben.common.util.HttpUtil;
import org.benben.common.util.MyStringUtils;
import org.benben.common.util.PayCommonUtils;
import org.benben.common.util.XMLUtil;
import org.benben.modules.business.commen.dto.WechatInfo;
import org.benben.modules.business.commen.dto.WechatRefund;
import org.benben.modules.business.commen.service.WechatpayServiceI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.net.www.protocol.https.DefaultHostnameVerifier;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Service("wechatPayService")
public class WechatpayServiceImpl implements WechatpayServiceI {


//	@Value("${WECHAT_APP_ID}")
	private String appId = "wx29264b38531d6be5";
//	@Value("${WECHAT_APP_SECRET}")
	private String appSecret = "953e1c27751a625c3d8b116b63457541";
//	@Value("${WECHAT_MCH_ID}")
	private String machId = "1520562931";
//	@Value("${WECHAT_API_KEY}")
	private String apiKey = "penpen20181214penpen181818penpen";


	@Override
	public Map<String, Object> pay(WechatInfo info) {

		SortedMap<String,Object> params = new TreeMap<>();
        String spbill_create_ip = info.getSpbillCreateIp();
        //String nonce_str = MyRandom.randomInteger(28);
        String nonce_str =  MyStringUtils.nonceStr();//MyRandom.randomInteger(32);
        params.put("appid", appId);
        params.put("mch_id", machId);
        params.put("nonce_str", nonce_str);
        params.put("body", info.getBody());//product.getName()
        params.put("out_trade_no", info.getOutTradeNo());
        params.put("total_fee", info.getTotalFee());
        params.put("spbill_create_ip", spbill_create_ip);
        params.put("notify_url", info.getNotifyUrl());
        params.put("trade_type", "JSAPI");
        //params.put("appid", wechatAppId);

        System.out.println("after save order------");
		System.out.println(apiKey);
        String sign = PayCommonUtils.createSign("utf8", params, apiKey);
        params.put("sign", sign);
        String requestXml = PayCommonUtils.getRequestXml(params);

        String postData = HttpUtil.postData(
                "https://api.mch.weixin.qq.com/pay/unifiedorder", requestXml);

        System.out.println(postData);
        String resXml = postData;
        Map<String, Object> map = null;
        try {
            map = XMLUtil.doXMLParse(resXml);
        } catch (Exception e) {
            e.printStackTrace();
            /*returnMap.put("flag", ReturnData.FAIL);
            return returnMap;*/
            return null;
        }
        String prepay_id = (String) map.get("prepay_id");
        String times = System.currentTimeMillis() + "";

        SortedMap<String,Object> packageParams = new TreeMap<>();
        prepay_id = "prepay_id="+prepay_id;
        packageParams.put("timestamp", times);
        packageParams.put("nonceStr", MyStringUtils.nonceStr());
        packageParams.put("package", prepay_id);
        packageParams.put("signType", "MD5");

        String paySign = PayCommonUtils.createSign("UTF-8", packageParams,
                apiKey);
        packageParams.put("paySign", paySign);
        packageParams.put("packagete", prepay_id);
        packageParams.put("flag", "success");
        //----------------------
        System.out.println(prepay_id);

		for (String key : packageParams.keySet()) {
			System.out.println("value: = " + packageParams.get(key)+ "key = "+ key);
		}
		for (String key : map.keySet()) {
			System.out.println("value: = " + packageParams.get(key)+ "key = "+ key);
		}
		System.out.println(map);
        return map;
	}

/*	@Override
	public PayData closeorder(String type, String orderNo) {
		if(StringUtils.isEmpty(orderNo)) {
			return new PayData(PayData.ERROR, "订单号不能为空！");
		}
		SortedMap<String, Object> params = new TreeMap<>();
		params.put("appid", WechatConfig.getAppId());
		params.put("mch_id", WechatConfig.getMchId());
		params.put("out_trade_no", orderNo);
		params.put("nonce_str", "");
		
		String sign = PayCommonUtils.createSign("UTF-8", params, WechatConfig.getPartnerKey());
        params.put("sign", sign);
        params.put("sign_type", "MD5");
        
        String requestXml = PayCommonUtils.getRequestXml(params);

        String postData = HttpUtil.postData(
                "https://api.mch.weixin.qq.com/pay/closeorder", requestXml);

        System.out.println(postData);
        String resXml = postData;
        Map<String, Object> map = null;
        try {
            map = XMLUtil.doXMLParse(resXml);
        } catch (Exception e) {
            e.printStackTrace();
            return new PayData(PayData.ERROR,"调用关闭订单失败！");
        }
        PayData returnData = null;
        if(PayData.SUCCESS.equals(map.get("return_code").toString())) {
        	returnData = new PayData(PayData.SUCCESS, "关闭订单成功！");
        }else {
        	returnData = new PayData(PayData.ERROR, "调用关闭订单失败！");
        }
        returnData.setMap(map);
		return returnData;
	}*/
	

	@Override
	public Map<String, Object> refund( WechatRefund refund) {
		SortedMap<String, Object> map = new TreeMap<String, Object>();
        //商户平台id
        map.put("mch_id", machId);
        //微信分配的公众账号ID（企业号corpid即为此appId）
        map.put("appid", appId);
        //随机字符串
        map.put("nonce_str", MyStringUtils.nonceStr());
        //商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
        // 获取用户押金订单号（用户在微信支付时的订单号）
        String outTradeNo = refund.getOutTradeNo();
        
        map.put("out_trade_no", outTradeNo);//微信订单号  内部订单号和微信订单号二选一
        //商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
        String outRefundNo = refund.getOutRefundNo();//CheckUtil.create_timestamp();
        map.put("out_refund_no", outRefundNo);
      //订单总金额，单位为分，只能为整数（用户在微信支付时的总金额）
        String totalFee = refund.getWechatTotalFee();//fee.intValue() + "";
        map.put("total_fee", totalFee);
        //退款总金额，单位为分，只能为整数（不能大于订单总金额）
        map.put("refund_fee", refund.getWechatRefundFee());
        if(!StringUtils.isEmpty(refund.getRefundDesc())) {
        	map.put("refund_desc", refund.getRefundDesc());
        }
        //异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数
        //如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效
        
        map.put("notify_url", "baseserver"+refund.getNotifyUrl());
        String sign = PayCommonUtils.createSign("UTF-8", map, apiKey);
        //String sign = CheckUtil.generateSignature(map, key, "MD5");
        //签名
        map.put("sign", sign);
        //String xml = CheckUtil.getXMLFromMap(map);
        String xml = PayCommonUtils.getRequestXml(map);
//        PayData rdata = new PayData();
        try {
			//获取apiclient_cert.p12证书，以下内容是直接在微信提供的demo中摘取出来的
        	File file = new File(File.separator + "api"+ File.separator + "apiclient_cert.p12");
        	FileInputStream fileInputStream = new FileInputStream(file);
            InputStream certStream = fileInputStream;//request.getServletContext().getResourceAsStream("/WEB-INF/apiclient_cert.p12");

            char[] password = machId.toCharArray();

            KeyStore ks = KeyStore.getInstance("PKCS12");
            ks.load(certStream, password);
            // 实例化密钥库 & 初始化密钥工厂
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(ks, password);
            // 创建 SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslContext,
                    new String[]{"TLSv1"},
                    null,
                    new DefaultHostnameVerifier());
            BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
                            .register("https", sslConnectionSocketFactory)
                            .build(),
                    null,
                    null,
                    null
            );
            HttpClient httpClient = HttpClientBuilder.create()
                    .setConnectionManager(connManager)
                    .build();
            String url = "https://api.mch.weixin.qq.com/secapi/pay/refund";
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
            httpPost.setConfig(requestConfig);
            StringEntity postEntity = new StringEntity(xml, "UTF-8");
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + machId);
            httpPost.setEntity(postEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            String data = EntityUtils.toString(httpEntity, "UTF-8");
            //Map<String, String> refounResult = CheckUtil.xmlToMap(data);
            Map<String, Object> refounResult = XMLUtil.doXMLParse(data);
            //logger.info("退款返回的信息" + refounResult);	
            
            if ("SUCCESS".equals(refounResult.get("return_code")) && "SUCCESS".equals(refounResult.get("result_code"))) {
                // 获得退款详细信息，封装到数据库中
                outTradeNo = refounResult.get("out_trade_no").toString();//商户订单号
            } else {

            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        //return "wechat/return/refundSuccess";
		return map;
	}
}
