package org.benben.common.util;

import java.util.UUID;

public class MyStringUtils {


	public static String nonceStr() {
		return getOrderNo().toUpperCase();
	}

	public static String getOrderNo(){
		String string = UUID.randomUUID().toString();
		string = string.replaceAll("-", "");
		if(string.length()<32) {
			string += UUID.randomUUID().toString().replaceAll("-", "");
		}
		String result = string.substring(0, 32);
		return result;
	}
	
}
