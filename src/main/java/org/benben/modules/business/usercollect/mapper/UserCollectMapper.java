package org.benben.modules.business.usercollect.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.usercollect.entity.UserCollect;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.usercollect.vo.UserCollectVo;
import org.benben.modules.business.userposts.entity.Posts;

import javax.websocket.server.ServerEndpoint;

/**
 * @Description: 用户收藏表管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface UserCollectMapper extends BaseMapper<UserCollect> {

	@Select("select * from bb_posts where id = #{postsId}")
	Posts queryByPostsId(String postsId);

	@Select("select * from user_collect where user_id = #{uid}")
	List<UserCollectVo> querrUserPosts(String uid);

	@Select("select * from user_collect where posts_id = #{postId} and user_id = #{id}")
	UserCollect getCollect(@Param("postId") String postId, @Param("id") String id);

	@Select("select * from user_collect where posts_id = #{postId}")
	List<UserCollect> getPostIdCollect(String postId);
}
