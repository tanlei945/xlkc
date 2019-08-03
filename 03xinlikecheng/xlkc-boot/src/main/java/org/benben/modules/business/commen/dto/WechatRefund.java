package org.benben.modules.business.commen.dto;

import java.math.BigDecimal;

public class WechatRefund {

	/**
	 * 微信订单号 和商户订单号二选一
	 */
	private String transactionId;

	/**
	 * 商户订单号 和微信订单号二选一
	 */
	private String outTradeNo;

	/**
	 * 商户退款单号
	 */
	private String outRefundNo;

	/**
	 * 订单金额 以元为单位
	 */
	private BigDecimal totalFee;

	/**
	 * 退款金额 以元为单位
	 */
	private BigDecimal refundFee;

	/**
	 * 退款原因
	 */
	private String refundDesc;

	/**
	 * 退款结果通知url
	 */
	private String notifyUrl;

	public WechatRefund() {
	}
	
	public WechatRefund(String transactionId, String outRefundNo, BigDecimal totalFee, BigDecimal refundFee) {
		this.transactionId = transactionId;
		this.outRefundNo = outRefundNo;
		this.totalFee = totalFee;
		this.refundFee = refundFee;
	}

	

	public WechatRefund(String outRefundNo, BigDecimal totalFee, BigDecimal refundFee,String outTradeNo) {
		this.outTradeNo = outTradeNo;
		this.outRefundNo = outRefundNo;
		this.totalFee = totalFee;
		this.refundFee = refundFee;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getOutRefundNo() {
		return outRefundNo;
	}

	public void setOutRefundNo(String outRefundNo) {
		this.outRefundNo = outRefundNo;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public BigDecimal getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(BigDecimal refundFee) {
		this.refundFee = refundFee;
	}

	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getWechatTotalFee() {
		BigDecimal fund = this.totalFee.multiply(new BigDecimal(100));
		return Integer.toString(fund.intValue());
	}
	public String getWechatRefundFee() {
		BigDecimal fund = this.refundFee.multiply(new BigDecimal(100));
		return fund.intValue()+"";
	}
}
