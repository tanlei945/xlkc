package org.benben.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileVerify {

	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		String s2="^[1](([3][0-9])|([4][5,7,9])|([5][4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";// 验证手机号
		if(str != null){
			p = Pattern.compile(s2);
			m = p.matcher(str);
			b = m.matches();
		}
		return b;
	}

}
