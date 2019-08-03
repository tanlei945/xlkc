package org.benben.modules.business.usermessage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.userevaluate.entity.UserEvaluate;
import org.benben.modules.business.usermessage.entity.UserMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.usermessage.vo.UserMessageVo;

/**
 * @Description: 用户消息管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface UserMessageMapper extends BaseMapper<UserMessage> {

	@Select("SELECT * from user_message where state = #{state} and user_id = #{uid} order by create_time desc")
	List<UserMessage> queryByState(@Param("state") Integer state,@Param("uid") String uid);

	@Select("  SELECT m.*,u.nickname from `user` u INNER JOIN user_message m on m.user_id = u.id where m.state = #{state} and user_id =#{uid} order by create_time desc")
	List<UserMessageVo> queryByLike(@Param("state") Integer state,@Param("uid") String uid);

	@Select("select * from user_evaluate where id = #{evaluateId}")
	UserEvaluate queryByEvaluateId(String evaluateId);

	@Select("select * from user_message where type = 0 and user_id = #{uid}")
	List<UserMessage> getMessageNum(String uid);

	@Select("SELECT m.*,u.nickname from `user` u INNER JOIN user_message m on m.user_id = u.id where m.state = #{state} and user_id =#{uid} and m.create_time = (SELECT MAX(create_time) from user_message where state =#{state} and user_id =#{uid})")
	UserMessageVo queryNewLike(Integer state, String uid);
}
