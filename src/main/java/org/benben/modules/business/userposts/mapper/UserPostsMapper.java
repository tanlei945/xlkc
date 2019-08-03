package org.benben.modules.business.userposts.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.userposts.entity.Posts;
import org.benben.modules.business.userposts.entity.UserPosts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.userposts.vo.UserPostsVo;

/**
 * @Description: 用户帖子管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface UserPostsMapper extends BaseMapper<UserPosts> {

	@Select("select * from user_posts u INNER JOIN bb_posts p on u.posts_id = p.id where u.user_id = #{uid} ORDER BY u.create_time DESC ")
	List<UserPostsVo> queryUserPosts(String uid);

	@Select("select * from bb_posts where id = #{postsId}")
	Posts queryByPostsId(String postsId);

	@Select("select * from user_posts where posts_id = #{postId}")
	UserPosts getUserPosts(String postId);
}
