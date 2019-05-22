package org.benben.modules.business.userposts.service;

import org.benben.modules.business.userposts.entity.Posts;
import org.benben.modules.business.userposts.entity.UserPosts;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.userposts.vo.UserPostsVo;

import java.util.List;

/**
 * @Description: 我的帖子表
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
public interface IUserPostsService extends IService<UserPosts> {

	List<UserPostsVo> queryUserPosts();

	Posts queryByPostsId(String postsId);
}
