package org.benben.modules.business.usermessage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.usermessage.entity.UserMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.usermessage.vo.UserMessageVo;

/**
 * @Description: 用户消息表
 * @author： jeecg-boot
 * @date：   2019-05-20
 * @version： V1.0
 */
public interface UserMessageMapper extends BaseMapper<UserMessage> {

	@Select("SELECT * from user_message where state = #{state}")
	List<UserMessage> queryByState(Integer state);

	List<UserMessageVo> queryByLike(Integer state);
}
