package org.benben.modules.business.user.mapper;


import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.user.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 普通用户
 * @author： jeecg-boot
 * @date：   2019-04-20
 * @version： V1.0
 */
public interface UserMapper extends BaseMapper<User> {

	@Select("SELECT * from `user` where chinaname = #{chinaname} and englishname = #{englishname} and referrer = #{referrer}")
	User verifyUser(String chinaname, String englishname, String referrer);
}
