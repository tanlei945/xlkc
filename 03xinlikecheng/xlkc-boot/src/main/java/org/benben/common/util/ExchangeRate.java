package org.benben.common.util;

import java.math.BigDecimal;

/**
 * 人民币到港币的转换
 */
public class ExchangeRate {
	public static void main(String[] args) {
		System.out.println(exchangeRate(new BigDecimal(2000)));
	}

	public static BigDecimal exchangeRate(BigDecimal chinaMoney){
		BigDecimal HKMoney;
		BigDecimal num = new BigDecimal("1.1307");
		HKMoney = chinaMoney.multiply(num);
		return  HKMoney;
	}
}
