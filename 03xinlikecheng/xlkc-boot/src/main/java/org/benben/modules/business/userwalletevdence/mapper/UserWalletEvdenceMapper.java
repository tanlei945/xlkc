package org.benben.modules.business.userwalletevdence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.userwalletevdence.entity.UserWalletEvdence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.userwalletevdence.vo.UserWalletEvdenceVo;

/**
 * @Description: 钱包凭证管理
 * @author： jeecg-boot
 * @date：   2019-07-10
 * @version： V1.0
 */
public interface UserWalletEvdenceMapper extends BaseMapper<UserWalletEvdence> {

	@Select("select * from user_wallet_evdence where uid = #{userId}")
	UserWalletEvdenceVo accountBackNow(String userId);
}
