package org.benben.modules.business.userwalletevdence.service.impl;

import org.benben.modules.business.userwalletevdence.entity.UserWalletEvdence;
import org.benben.modules.business.userwalletevdence.mapper.UserWalletEvdenceMapper;
import org.benben.modules.business.userwalletevdence.service.IUserWalletEvdenceService;
import org.benben.modules.business.userwalletevdence.vo.UserWalletEvdenceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 钱包凭证管理
 * @author： jeecg-boot
 * @date：   2019-07-10
 * @version： V1.0
 */
@Service
public class UserWalletEvdenceServiceImpl extends ServiceImpl<UserWalletEvdenceMapper, UserWalletEvdence> implements IUserWalletEvdenceService {

	@Autowired
	private UserWalletEvdenceMapper userWalletEvdenceMapper;

	@Override
	public UserWalletEvdenceVo accountBackNow(String userId) {
		return userWalletEvdenceMapper.accountBackNow(userId);
	}
}
