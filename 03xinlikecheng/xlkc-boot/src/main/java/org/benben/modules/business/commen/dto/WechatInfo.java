package org.benben.modules.business.commen.dto;

import java.math.BigDecimal;

public class WechatInfo {


	/**
	 * 商品描述  名字
	 * 是否必填：是
	 */
	private String body;

	/**
	 * 商品详情
	 * 是否必填：否
	 */
	private String detail;

	/**
	 * 附加数据
	 * 是否必填：否
	 */
	private String attach;
	
	/**
	 * 订单号   
	 * 是否必填：是
	 */
	private String outTradeNo;
	
	/**
	 * 商品价格
	 * 是否必填：是
	 */
	private BigDecimal totalFee;

	/**
	 * 通知地址
	 */
	private String notifyUrl;

	/**
	 * 交易类型
	 */
	private String tradeType;

	/**
	 * 终端IP 不填写默认显示服务器ip
	 * 是否必填：是
	 */
	private String spbillCreateIp = "47.106.98.95";

	private String openid;



	public String getBody() {
		return body;
	}

	public String getDetail() {
		return detail;
	}

	public String getAttach() {
		return attach;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public String getTradeType() {
		return tradeType;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public String getOpenid() {
		return openid;
	}


	public void setBody(String body) {
		this.body = body;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
