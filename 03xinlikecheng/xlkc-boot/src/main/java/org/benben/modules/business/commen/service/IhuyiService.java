package org.benben.modules.business.commen.service;

public interface IhuyiService {
	Integer sendIhuyi(String areacode, String mobile,String event);
	boolean check(String mobile, String event, Integer verify);
}
