package org.benben.modules.business.verrifycode.service.impl;

import org.benben.modules.business.verrifycode.entity.Verifycode;
import org.benben.modules.business.verrifycode.mapper.VerifycodeMapper;
import org.benben.modules.business.verrifycode.service.IVerifycodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 验证码
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Service
public class VerifycodeServiceImpl extends ServiceImpl<VerifycodeMapper, Verifycode> implements IVerifycodeService {

	@Autowired
	private VerifycodeMapper verifycodeMapper;

	@Override
	public Verifycode queryByVerify(int verify) {
		return verifycodeMapper.queryByVerify(verify);
	}
}
