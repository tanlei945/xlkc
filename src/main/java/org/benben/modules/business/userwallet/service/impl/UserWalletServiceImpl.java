package org.benben.modules.business.userwallet.service.impl;

import org.benben.modules.business.userwallet.entity.UserWallet;
import org.benben.modules.business.userwallet.mapper.UserWalletMapper;
import org.benben.modules.business.userwallet.service.IUserWalletService;
import org.benben.modules.business.userwallet.vo.UserWalletVo;
import org.benben.modules.business.userwalletevdence.entity.UserWalletEvdence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户钱包管理
 * @author： jeecg-boot
 * @date：   2019-07-08
 * @version： V1.0
 */
@Service
public class  UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements IUserWalletService {

	@Autowired
	private UserWalletMapper userWalletMapper;

	@Override
	public UserWalletVo queryByUserId(String id) {
		UserWalletVo userWalletVo = userWalletMapper.queryByUid(id);
		UserWalletEvdence picture = userWalletMapper.getPicture(id);
		userWalletVo.setPicture(picture.getPicture());
		return userWalletVo;
	}

}
