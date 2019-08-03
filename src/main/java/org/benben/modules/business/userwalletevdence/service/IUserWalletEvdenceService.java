package org.benben.modules.business.userwalletevdence.service;

import org.benben.modules.business.userwalletevdence.entity.UserWalletEvdence;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.userwalletevdence.vo.UserWalletEvdenceVo;

/**
 * @Description: 钱包凭证管理
 * @author： jeecg-boot
 * @date：   2019-07-10
 * @version： V1.0
 */
public interface IUserWalletEvdenceService extends IService<UserWalletEvdence> {

	UserWalletEvdenceVo accountBackNow(String userId);
}
