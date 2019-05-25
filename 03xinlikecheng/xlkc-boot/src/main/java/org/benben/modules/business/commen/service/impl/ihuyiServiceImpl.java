package org.benben.modules.business.commen.service.impl;

import com.google.common.base.Verify;
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

	/**
	 * 给手机号发送验证码，并保存验证码
	 * @param mobile
	 * @return
	 */
	@Override
	public int sendIhuyi(String mobile) {

		int verify = Sendsms.sendVerify(mobile);
		Verifycode verifycode = new Verifycode();
		verifycode.setVerify(verify);
		verifycodeService.save(verifycode);
		return verify;

	}

	/**
	 * 检验验证码
	 * @param verify
	 * @return
	 */
	@Override
	public boolean check(int verify) {

		Verifycode verifycode = verifycodeService.queryByVerify(verify);
		if (verifycode == null) {
			return false;
		} else {
			return true;
		}
	}

}
