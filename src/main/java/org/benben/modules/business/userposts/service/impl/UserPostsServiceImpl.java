package org.benben.modules.business.userposts.service.impl;

import org.benben.modules.business.course.vo.Courses;
import org.benben.modules.business.userposts.entity.Posts;
import org.benben.modules.business.userposts.entity.UserPosts;
import org.benben.modules.business.userposts.mapper.UserPostsMapper;
import org.benben.modules.business.userposts.service.IUserPostsService;
import org.benben.modules.business.userposts.vo.UserPostsVo;
import org.benben.modules.business.userposts.vo.UserPostsVos;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 用户帖子管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Service
public class UserPostsServiceImpl extends ServiceImpl<UserPostsMapper, UserPosts> implements IUserPostsService {

	@Autowired
	private UserPostsMapper userPostsMapper;

	@Override
	public List<UserPostsVos> queryUserPosts(String uid) {
		List<UserPostsVo> userPosts = userPostsMapper.queryUserPosts(uid);
		List<UserPostsVos> userPostsVos = new ArrayList<>();
		for (UserPostsVo userPost : userPosts) {
			UserPostsVos userPostsVo = new UserPostsVos();

			String bookUrl = userPost.getIntroImg();
			List<String> list = new ArrayList<>();
			list = Arrays.asList(bookUrl.split(","));
			BeanUtils.copyProperties(userPost,userPostsVo);
			userPostsVo.setIntroImg(list);
			userPostsVos.add(userPostsVo);

		}
		return userPostsVos;
	}

	@Override
	public Posts queryByPostsId(String postsId) {
		Posts posts = userPostsMapper.queryByPostsId(postsId);
		return posts;
	}

	@Override
	public UserPosts getByPostId(String postId) {
		return userPostsMapper.getUserPosts(postId);
	}
}
