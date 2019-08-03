package org.benben.common.util;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONObject;
import org.benben.modules.business.banner.entity.Banner;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

/*	public  static void main(String[] args){
		String[] ids = {"1","2","3","4"};
		String[] imgs = {"asd","aaa","sss","ccc"};
		System.out.println(imgUrl(ids,imgs));
		Map mapTypes = JSON.parseObject(imgUrl(ids,imgs));
		for (Object o : mapTypes.keySet()) {
			System.out.println(o);
			System.out.println("value值：" + mapTypes.get(o));
		}
	}*/

	public static String imgUrl(String[] ids, String[] imgs) {
		Map<String,String> map = new HashMap<>();
		for (int i = 0; i < ids.length && i< imgs.length; i++) {
			map.put(ids[i],imgs[i]);
		}
		System.out.println(ids.length);
		JSONObject jsonObject = JSONObject.fromObject(map);
		String imgurl = jsonObject.toString();
		return imgurl;
	}
}
