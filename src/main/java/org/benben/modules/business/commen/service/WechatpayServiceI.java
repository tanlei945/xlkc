package org.benben.modules.business.commen.service;

import org.benben.modules.business.commen.dto.WechatInfo;
import org.benben.modules.business.commen.dto.WechatRefund;

import java.util.Map;

public interface WechatpayServiceI {

	/**
	 * 调取微信支付
	 * @param info
	 * @return
	 */
	public Map<String, Object> pay(WechatInfo info);
	
	/**
	 * 关闭订单
	 * @param type
	 * @param orderNo
	 * @return
	 */
//	public PayData closeorder(String type, String orderNo);
	
	/**
	 * 微信退款
	 * @param refund
	 * @return
	 */
	public Map<String,Object> refund(WechatRefund refund);
	
}
