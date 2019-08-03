package org.benben.modules.business.commen.service.impl;

import com.google.common.base.Verify;
import org.apache.commons.lang.StringUtils;
import org.benben.common.util.RedisUtil;
import org.benben.common.util.Sendsms;
import org.benben.modules.business.commen.service.IhuyiService;
import org.benben.modules.business.verrifycode.entity.Verifycode;
import org.benben.modules.business.verrifycode.service.IVerifycodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ihuyiServiceImpl implements IhuyiService {
	@Autowired
	private IVerifycodeService verifycodeService;

	@Autowired
	private  RedisUtil redisUtil;

	/**
	 * 给手机号发送验证码，并保存验证码
	 * @param mobile
	 * @return
	 */
	@Override
	public Integer sendIhuyi( String areacode, String mobile,String event) {

		String phone = areacode +" "+ mobile;
		int verify = Sendsms.sendVerify(phone);
		redisUtil.set(mobile+","+ event,verify,300);
		return verify;

	}

	/**
	 * 检验验证码
	 * @param verify
	 * @return
	 */
	@Override
	public boolean check(String mobile, String event ,Integer verify) {

		int num = 0;
		//检测redis是否过期
		if (!redisUtil.hasKey(mobile + "," + event)) {
			return false;
		}

		Integer result = (Integer) redisUtil.get(mobile + "," + event);
		String results = result + "";
		String verifys = verify + "";
		//取redis中缓存数据
		if (!StringUtils.equals(results, verifys) || result == null) {
			return false;
		}
//		//验证成功,删除redis缓存数据
//		redisUtil.del(mobile + "," + event);

		return true;
	}

}
