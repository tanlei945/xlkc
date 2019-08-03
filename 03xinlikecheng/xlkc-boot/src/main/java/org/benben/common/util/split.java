package org.benben.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class split {
	public static void main(String[] args) {
		String s = "aa";
		List<String> list = new ArrayList<>();
		list= Arrays.asList(s.split(","));
		System.out.println(list);
	}
}
