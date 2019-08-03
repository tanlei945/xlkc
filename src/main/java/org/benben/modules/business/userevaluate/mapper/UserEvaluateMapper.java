package org.benben.modules.business.userevaluate.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.userevaluate.entity.UserEvaluate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.userevaluate.vo.UserEvaluateVo;

/**
 * @Description: 用户评论表管理
 * @author： jeecg-boot
 * @date：   2019-06-11
 * @version： V1.0
 */
public interface UserEvaluateMapper extends BaseMapper<UserEvaluate> {

	@Select("select * from user_evaluate where posts_id = #{postId} and (parent_id is null or parent_id = '') order by create_time asc")
	List<UserEvaluateVo> queryByPostId(String postId);

	@Select("select * from user where id = #{userId}")
	User queryUserId(String userId);

	@Select("select * from user_evaluate where parent_id = #{id} and posts_id= #{postId} order by create_time asc")
	List<UserEvaluateVo> queryById(String id,String postId);

	@Select("select count(*) from bb_like where evaluate_id = #{id}")
	Integer getLikeNum(String id);

	@Select("select count(1) from user_response  where evaluate_id = #{id}")
	Integer getEvaluateNum(String id);
}
