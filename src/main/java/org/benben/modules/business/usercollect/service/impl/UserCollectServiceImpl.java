package org.benben.modules.business.usercollect.service.impl;

import org.benben.modules.business.posts.service.IPostsService;
import org.benben.modules.business.recommendposts.entity.RecommendPosts;
import org.benben.modules.business.recommendposts.service.IRecommendPostsService;
import org.benben.modules.business.usercollect.entity.UserCollect;
import org.benben.modules.business.usercollect.mapper.UserCollectMapper;
import org.benben.modules.business.usercollect.service.IUserCollectService;
import org.benben.modules.business.usercollect.vo.UserCollectVo;
import org.benben.modules.business.userposts.entity.Posts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 用户收藏表管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements IUserCollectService {

	@Autowired
	private UserCollectMapper collectMapper;
	@Autowired
	private IPostsService postsService;
	@Autowired
	private IRecommendPostsService recommendPostsService;

	@Override
	public List<UserCollectVo> queryUserPosts(String uid) {
		List<UserCollectVo> userCollectVos = collectMapper.querrUserPosts(uid);
		for (UserCollectVo userCollectVo : userCollectVos) {
			org.benben.modules.business.posts.entity.Posts posts = postsService.getById(userCollectVo.getPostsId());
			if (posts != null) {
				userCollectVo.setContent(posts.getContent());
				userCollectVo.setIntroImg(posts.getIntroImg());
				userCollectVo.setPostsName(posts.getPostsName());
			} else {
				RecommendPosts recommendPosts  = recommendPostsService.getById(userCollectVo.getPostsId());
				userCollectVo.setPostsName(recommendPosts.getName());
				userCollectVo.setContent(recommendPosts.getContent());
				userCollectVo.setIntroImg(recommendPosts.getIntroImg());
			}

		}
		return userCollectVos;
	}

	@Override
	public Posts queryByPostsId(String postsId) {
		Posts posts = collectMapper.queryByPostsId(postsId);
		return posts;
	}

	@Override
	public UserCollect getCollect(String postId, String uid) {
		UserCollect collect = collectMapper.getCollect(postId,uid);
		return collect;
	}

	@Override
	public List<UserCollect> getPostIdCollect(String postId) {
		return collectMapper.getPostIdCollect(postId);
	}
}
