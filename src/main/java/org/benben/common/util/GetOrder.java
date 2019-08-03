package org.benben.common.util;

import com.mchange.lang.IntegerUtils;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GetOrder {
	public static void main(String[] args) {

		List<Integer> list = new ArrayList<>();
		String s = "sss";

		list.add(1);
		list.add(1);
		list.add(1);
		list.add(1);
		for (Integer integer : list) {
			s = s + integer+ ",";
		}
		int[] i = new int[list.size()];
//		i = IntegerUtils.(list,",")
		System.out.println(s);
		String ddd = getOrderIdByTime("xlkcxlkc");
		System.out.println(ddd);
	}

	/**
	 * 生成订单编号
	 * @param uid
	 * @return
	 */
	public static String getOrderIdByTime(String uid) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String newDate=sdf.format(new Date());
		String result="";
		Random random=new Random();
		for(int i=0;i<3;i++){
			result+=random.nextInt(10);
		}
		return newDate+result;
	}
}
