package org.benben.modules.business.recommendposts.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.recommendposts.entity.RecommendPosts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.usercollect.entity.UserCollect;

/**
 * @Description: 管理员推荐帖子
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
public interface RecommendPostsMapper extends BaseMapper<RecommendPosts> {

	@Select("SELECT * from bb_recommend_posts where type = 1 ORDER BY create_time DESC limit #{pageNumber}, #{pageSize}")
	List<RecommendPosts> queryRecommendPosts(@Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("SELECT count(1) from bb_recommend_posts where type = 1 ORDER BY create_time DESC")
	Integer queryRecommendPostsAll();

	@Select("select count(1) from bb_like where post_id = #{id}")
	Integer getLikeNum(String id);

	@Select("select count(1) from user_evaluate where posts_id = #{id}")
	Integer getEvaluateNum(String id);

	@Select("SELECT * from bb_recommend_posts where type = 1 and create_time = (SELECT MAX(create_time)  from bb_recommend_posts where type = 1 )")
	RecommendPosts challengePosts();

	@Select("select type from bb_like where uid = #{uid} and post_id = #{postId}")
	Integer getLikeType(String uid, String postId);

	@Select("select * from user_collect where user_id = #{uid} and posts_id = #{postId}")
	UserCollect getCollectType(@Param("uid") String uid,@Param("postId") String postId);
}
