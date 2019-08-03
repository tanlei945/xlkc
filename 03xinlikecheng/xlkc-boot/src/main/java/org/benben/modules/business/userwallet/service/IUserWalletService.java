package org.benben.modules.business.userwallet.service;

import org.benben.modules.business.userwallet.entity.UserWallet;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.userwallet.vo.UserWalletVo;

/**
 * @Description: 用户钱包管理
 * @author： jeecg-boot
 * @date：   2019-07-08
 * @version： V1.0
 */
public interface IUserWalletService extends IService<UserWallet> {

	UserWalletVo queryByUserId(String id);
}
