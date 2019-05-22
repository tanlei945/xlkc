package org.benben.modules.business.userposts.service.impl;

import javafx.geometry.Pos;
import org.benben.modules.business.userposts.entity.Posts;
import org.benben.modules.business.userposts.entity.UserPosts;
import org.benben.modules.business.userposts.mapper.UserPostsMapper;
import org.benben.modules.business.userposts.service.IUserPostsService;
import org.benben.modules.business.userposts.vo.UserPostsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 我的帖子表
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
@Service
public class UserPostsServiceImpl extends ServiceImpl<UserPostsMapper, UserPosts> implements IUserPostsService {

	@Autowired
	private UserPostsMapper userPostsMapper;

	@Override
	public List<UserPostsVo> queryUserPosts() {
		List<UserPostsVo> userPosts = userPostsMapper.queryUserPosts();

		return userPosts;
	}

	@Override
	public Posts queryByPostsId(String postsId) {
		Posts posts = userPostsMapper.queryByPostsId(postsId);
		return posts;
	}
}
