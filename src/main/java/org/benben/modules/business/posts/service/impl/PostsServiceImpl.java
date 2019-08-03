package org.benben.modules.business.posts.service.impl;

import io.swagger.models.auth.In;
import org.apache.shiro.SecurityUtils;
import org.benben.modules.business.posts.entity.Posts;
import org.benben.modules.business.posts.mapper.PostsMapper;
import org.benben.modules.business.posts.service.IPostsService;
import org.benben.modules.business.posts.vo.PostsVo;
import org.benben.modules.business.posts.vo.PostsVos;
import org.benben.modules.business.recommendposts.vo.RecommendsVos;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usercollect.entity.UserCollect;
import org.benben.modules.system.entity.SysUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 帖子的管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements IPostsService {

	@Autowired
	private PostsMapper postsMapper;

	private PostsServiceImpl postsService ;

	@Override
	public PostsVo listNewPosts(Integer pageNumber, Integer pageSize) {
		List<Posts> posts = postsMapper.queryNewPosts(pageNumber,pageSize);
		List<PostsVos> postsVos = new ArrayList<>();

		for (Posts post : posts) {
			Integer likeNum = postsMapper.getLikeNum(post.getId());
			post.setLiknum(likeNum);
			Integer evaluateNum = postsMapper.getEvaluateNum(post.getId());
			post.setCommentNum(evaluateNum);

			//查询点赞的状态
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			Integer likeType = postsMapper.getLikeType(user.getId(), post.getId());
			if (likeType == null) {
				post.setLikeType(0);
			} else {
				post.setLikeType(1);
			}
			//查询是否收藏
			UserCollect collect = postsMapper.getCollectType(user.getId(), post.getId());
			if (collect == null) {
				post.setCollectType(0);
			} else {
				post.setCollectType(1);
			}

			postsMapper.updateById(post);

			//把图片转换为数组
			PostsVos postsVos1 = new PostsVos();
			String introImg = post.getIntroImg();
			List<String> list = new ArrayList<>();
			list = Arrays.asList(introImg.split(","));

			BeanUtils.copyProperties(post,postsVos1);
			postsVos1.setIntroImg(list);
			postsVos.add(postsVos1);

		}

		Integer total = postsMapper.queryNewPostsAll();
		if (total == null) {
			total = 0;
		}
		PostsVo postsVo = new PostsVo();
		postsVo.setPostsList(postsVos);
		postsVo.setTotal(total);

		return postsVo;
	}

	@Override
	public PostsVo listHottestPosts(Integer pageNumber, Integer pageSize) {
		List<Posts> posts = postsMapper.queryHottestPosts(pageNumber,pageSize);
		List<PostsVos> postsVos = new ArrayList<>();

		for (Posts post : posts) {
			Integer likeNum = postsMapper.getLikeNum(post.getId());
			post.setLiknum(likeNum);
			Integer evaluateNum = postsMapper.getEvaluateNum(post.getId());
			post.setCommentNum(evaluateNum);

			//查询点赞的状态
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			Integer likeType = postsMapper.getLikeType(user.getId(), post.getId());
			if (likeType == null) {
				post.setLikeType(0);
			} else {
				post.setLikeType(1);
			}
			postsMapper.updateById(post);

			//查询是否收藏
			UserCollect collect = postsMapper.getCollectType(user.getId(), post.getId());
			if (collect == null) {
				post.setCollectType(0);
			} else {
				post.setCollectType(1);
			}

			//把图片转换为数组
			PostsVos postsVos1 = new PostsVos();
			String introImg = post.getIntroImg();
			List<String> list = new ArrayList<>();
			list = Arrays.asList(introImg.split(","));

			BeanUtils.copyProperties(post,postsVos1);
			postsVos1.setIntroImg(list);
			postsVos.add(postsVos1);

		}

		Integer total = postsMapper.queryHotestPostsAll();
		if (total == null) {
			total = 0;
		}
		PostsVo postsVo = new PostsVo();
		postsVo.setPostsList(postsVos);
		postsVo.setTotal(total);

		return postsVo;
	}

	@Override
	public PostsVo queryNewPosts(String name, Integer pageNumber, Integer pageSize) {
		List<Posts> posts = postsMapper.queryByName(name,pageNumber,pageSize);
		List<PostsVos> postsVos = new ArrayList<>();

		for (Posts post : posts) {
			Integer likeNum = postsMapper.getLikeNum(post.getId());
			post.setLiknum(likeNum);
			Integer evaluateNum = postsMapper.getEvaluateNum(post.getId());
			post.setCommentNum(evaluateNum);

			//查询点赞的状态
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			Integer likeType = postsMapper.getLikeType(user.getId(), post.getId());
			if (likeType == null) {
				post.setLikeType(0);
			} else {
				post.setLikeType(1);
			}
			postsMapper.updateById(post);

			//查询是否收藏
			UserCollect collect = postsMapper.getCollectType(user.getId(), post.getId());
			if (collect == null) {
				post.setCollectType(0);
			} else {
				post.setCollectType(1);
			}

			//把图片转换为数组
			PostsVos postsVos1 = new PostsVos();
			String introImg = post.getIntroImg();
			List<String> list = new ArrayList<>();
			list = Arrays.asList(introImg.split(","));

			BeanUtils.copyProperties(post,postsVos1);
			postsVos1.setIntroImg(list);
			postsVos.add(postsVos1);
		}

		Integer total = postsMapper.queryByNameAll(name);
		if (total == null) {
			total = 0;
		}
		PostsVo postsVo = new PostsVo();

		postsVo.setPostsList(postsVos);
		postsVo.setTotal(total);

		return postsVo;
	}

	@Override
	public PostsVo queryHottestPost(String name, Integer pageNumber, Integer pageSize) {
		List<Posts> posts = postsMapper.queryByNameHottest(name,pageNumber,pageSize);
		List<PostsVos> postsVos = new ArrayList<>();

		for (Posts post : posts) {
			Integer likeNum = postsMapper.getLikeNum(post.getId());
			post.setLiknum(likeNum);
			Integer evaluateNum = postsMapper.getEvaluateNum(post.getId());
			post.setCommentNum(evaluateNum);

			//查询点赞的状态
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			Integer likeType = postsMapper.getLikeType(user.getId(), post.getId());
			if (likeType == null) {
				post.setLikeType(0);
			} else {
				post.setLikeType(1);
			}
			postsMapper.updateById(post);

			//查询是否收藏
			UserCollect collect = postsMapper.getCollectType(user.getId(), post.getId());
			if (collect == null) {
				post.setCollectType(0);
			} else {
				post.setCollectType(1);
			}

			//把图片转换为数组
			PostsVos postsVos1 = new PostsVos();
			String introImg = post.getIntroImg();
			List<String> list = new ArrayList<>();
			list = Arrays.asList(introImg.split(","));

			BeanUtils.copyProperties(post,postsVos1);
			postsVos1.setIntroImg(list);
			postsVos.add(postsVos1);
		}

		Integer total = postsMapper.queryByNameHotestAll(name);
		if (total == null) {
			total = 0;
		}
		PostsVo postsVo = new PostsVo();
		postsVo.setPostsList(postsVos);
		postsVo.setTotal(total);

		return postsVo;
	}

	@Override
	public PostsVo queryCourseType(String courseTypeId, String name, Integer pageNumber, Integer pageSize) {
		List<Posts> posts = postsMapper.queryCourseType(courseTypeId, name,pageNumber,pageSize);
		List<PostsVos> postsVos = new ArrayList<>();

		//		List<Posts> posts = postsMapper.queryCourseType1();
		for (Posts post : posts) {
			Integer likeNum = postsMapper.getLikeNum(post.getId());
			post.setLiknum(likeNum);
			Integer evaluateNum = postsMapper.getEvaluateNum(post.getId());
			post.setCommentNum(evaluateNum);

			//查询点赞的状态
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			Integer likeType = postsMapper.getLikeType(user.getId(), post.getId());
			if (likeType == null) {
				post.setLikeType(0);
			} else {
				post.setLikeType(1);
			}

			//查询是否收藏
			UserCollect collect = postsMapper.getCollectType(user.getId(), post.getId());
			if (collect == null) {
				post.setCollectType(0);
			} else {
				post.setCollectType(1);
			}
			postsMapper.updateById(post);

			//把图片转换为数组
			PostsVos postsVos1 = new PostsVos();
			String introImg = post.getIntroImg();
			List<String> list = new ArrayList<>();
			list = Arrays.asList(introImg.split(","));

			BeanUtils.copyProperties(post,postsVos1);
			postsVos1.setIntroImg(list);
			postsVos.add(postsVos1);
		}

		Integer total = postsMapper.queryCourseTypeAll(courseTypeId, name);
		if (total == null) {
			total = 0;
		}
		PostsVo postsVo = new PostsVo();
		postsVo.setPostsList(postsVos);
		postsVo.setTotal(total);

		return postsVo;
	}

	@Override
	public PostsVo getHottestPosts(String courseTypeId, String name, Integer pageNumber, Integer pageSize) {
		List<Posts> posts = postsMapper.getHottestPosts(courseTypeId, name,pageNumber,pageSize);
		List<PostsVos> postsVos = new ArrayList<>();

		for (Posts post : posts) {
			Integer likeNum = postsMapper.getLikeNum(post.getId());
			post.setLiknum(likeNum);
			Integer evaluateNum = postsMapper.getEvaluateNum(post.getId());
			post.setCommentNum(evaluateNum);

			//查询点赞的状态
			User user = (User) SecurityUtils.getSubject().getPrincipal();
			Integer likeType = postsMapper.getLikeType(user.getId(), post.getId());
			if (likeType == null) {
				post.setLikeType(0);
			} else {
				post.setLikeType(1);
			}
			postsMapper.updateById(post);

			//查询是否收藏
			UserCollect collect = postsMapper.getCollectType(user.getId(), post.getId());
			if (collect == null) {
				post.setCollectType(0);
			} else {
				post.setCollectType(1);
			}

			//把图片转换为数组
			PostsVos postsVos1 = new PostsVos();
			String introImg = post.getIntroImg();
			List<String> list = new ArrayList<>();
			list = Arrays.asList(introImg.split(","));

			BeanUtils.copyProperties(post,postsVos1);
			postsVos1.setIntroImg(list);
			postsVos.add(postsVos1);
		}

		Integer total = postsMapper.getHottestPostsAll(courseTypeId,name);
		if (total == null) {
			total = 0;
		}
		PostsVo postsVo = new PostsVo();
		postsVo.setPostsList(postsVos);
		postsVo.setTotal(total);

		return postsVo;
	}

	@Override
	public List<String> hotSearch() {
		List<String> list = postsMapper.hotSearch();
		List<String> list1 = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			list1.add(list.get(i));
		}
		return list1;
	}
}
