package org.benben.modules.business.recommendposts.service.impl;

import org.apache.shiro.SecurityUtils;
import org.benben.modules.business.posts.vo.PostsVos;
import org.benben.modules.business.recommendposts.entity.RecommendPosts;
import org.benben.modules.business.recommendposts.mapper.RecommendPostsMapper;
import org.benben.modules.business.recommendposts.service.IRecommendPostsService;
import org.benben.modules.business.recommendposts.vo.RecommendPostsVo;
import org.benben.modules.business.recommendposts.vo.RecommendsVos;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usercollect.entity.UserCollect;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 管理员推荐帖子
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
@Service
public class RecommendPostsServiceImpl extends ServiceImpl<RecommendPostsMapper, RecommendPosts> implements IRecommendPostsService {

	@Autowired
	private RecommendPostsMapper postsMapper;

	@Override
	public RecommendPostsVo listRecommendPosts(Integer pageNumber, Integer pageSize) {
		List<RecommendPosts> recommendPosts = postsMapper.queryRecommendPosts(pageNumber, pageSize);
		List<RecommendsVos> recommendsVosList = new ArrayList<>();
		for (RecommendPosts recommendPost : recommendPosts) {
			Integer likeNum = postsMapper.getLikeNum(recommendPost.getId());
			recommendPost.setLikeNum(likeNum);
			Integer evaluateNum = postsMapper.getEvaluateNum(recommendPost.getId());
			recommendPost.setCommentNum(evaluateNum);
			postsMapper.updateById(recommendPost);

			/*//获取用户点赞状态
			UserPostsVos user = (UserPostsVos) SecurityUtils.getSubject().getPrincipal();
			Integer likeType = postsMapper.getLikeType(user.getId(), recommendPost.getId());
			recommendPost.setLikeType(likeType);*/

			//查询点赞的状态
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			Integer likeType = postsMapper.getLikeType(user.getId(), recommendPost.getId());
			if (likeType == null) {
				recommendPost.setLikeType(0);
			} else {
				recommendPost.setLikeType(1);
			}
			postsMapper.updateById(recommendPost);

			//查询是否收藏
			UserCollect collect = postsMapper.getCollectType(user.getId(), recommendPost.getId());
			if (collect == null) {
				recommendPost.setCollectType(0);
			} else {
				recommendPost.setCollectType(1);
			}

			//把图片转换为数组
			RecommendsVos recommendsVos = new RecommendsVos();
			String introImg = recommendPost.getIntroImg();
			List<String> list = new ArrayList<>();
			list = Arrays.asList(introImg.split(","));

			BeanUtils.copyProperties(recommendPost,recommendsVos);
			recommendsVos.setIntroImg(list);
			recommendsVosList.add(recommendsVos);

		}
		Integer total = postsMapper.queryRecommendPostsAll();
		if (total == null) {
			total = 0;
		}
		RecommendPostsVo recommendPostsVo = new RecommendPostsVo();
		recommendPostsVo.setTotal(total);
		recommendPostsVo.setPostsList(recommendsVosList);

		return recommendPostsVo;
	}

	@Override
	public RecommendPosts challengePosts() {
		return postsMapper.challengePosts();
	}
}
