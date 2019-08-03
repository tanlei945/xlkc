package org.benben.modules.business.userwallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.userwallet.entity.UserWallet;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.userwallet.vo.UserWalletVo;
import org.benben.modules.business.userwalletevdence.entity.UserWalletEvdence;

/**
 * @Description: 用户钱包管理
 * @author： jeecg-boot
 * @date：   2019-07-08
 * @version： V1.0
 */
public interface UserWalletMapper extends BaseMapper<UserWallet> {

	@Select("select * from user_wallet where user_id = #{id}")
	UserWalletVo queryByUid(String id);

	@Select("select * from user_wallet_evdence where uid = #{id}")
	UserWalletEvdence getPicture(String id);
}
