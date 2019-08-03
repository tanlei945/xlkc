package org.benben.modules.business.userposts.service;

import org.benben.modules.business.userposts.entity.Posts;
import org.benben.modules.business.userposts.entity.UserPosts;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.userposts.vo.UserPostsVo;
import org.benben.modules.business.userposts.vo.UserPostsVos;

import java.util.List;

/**
 * @Description: 用户帖子管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface IUserPostsService extends IService<UserPosts> {

	List<UserPostsVos> queryUserPosts(String uid );

	Posts queryByPostsId(String postsId);

	UserPosts getByPostId(String postId);
}
