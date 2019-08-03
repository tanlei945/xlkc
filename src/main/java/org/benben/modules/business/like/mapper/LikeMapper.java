package org.benben.modules.business.like.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.like.entity.Like;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 点赞管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
public interface LikeMapper extends BaseMapper<Like> {

	@Select("select type from bb_like where uid = #{uid} and post_id = #{postId}")
	Integer getLikeType(@Param("uid") String uid, @Param("postId") String postId);

	@Select("select * from bb_like where uid = #{uid} and post_id = #{postId}")
	List<Like> queryLike(@Param("uid") String uid, @Param("postId") String postId);

	@Select("select * from bb_like where uid = #{id} and evaluate_id = #{evaluateId}")
	List<Like> queryEvaluate(String id, String evaluateId);
}
