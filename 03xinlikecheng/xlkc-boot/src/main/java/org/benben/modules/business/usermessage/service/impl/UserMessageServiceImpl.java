package org.benben.modules.business.usermessage.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.benben.modules.business.posts.entity.Posts;
import org.benben.modules.business.posts.service.IPostsService;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.usercollect.service.IUserCollectService;
import org.benben.modules.business.userevaluate.entity.UserEvaluate;
import org.benben.modules.business.userevaluate.service.IUserEvaluateService;
import org.benben.modules.business.usermessage.entity.UserMessage;
import org.benben.modules.business.usermessage.mapper.UserMessageMapper;
import org.benben.modules.business.usermessage.service.IUserMessageService;
import org.benben.modules.business.usermessage.vo.EvaluateMessageVo;
import org.benben.modules.business.usermessage.vo.LikeVo;
import org.benben.modules.business.usermessage.vo.UserMessageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 用户消息管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {

	@Autowired
	private UserMessageMapper userMessageMapper;

	@Autowired
	private IUserService userService;

	@Autowired
	private IPostsService postsService;

	@Autowired
	private IUserEvaluateService evaluateService;

	@Autowired
	private IUserMessageService userMessageService;


	@Override
	public List<UserMessage> queryByState(Integer state, String uid) {
		List<UserMessage> list = userMessageMapper.queryByState(state,uid);
		for (UserMessage userMessage : list) {
			userMessage.setType(1);
			userMessageService.updateById(userMessage);
		}
		return list;
	}

	@Override
	public List<LikeVo> queryByLike(Integer state, User user) {
		List<UserMessageVo> userMessageVos = userMessageMapper.queryByLike(state,user.getId());
		List<LikeVo> likeVoList = new ArrayList<>();
		for (UserMessageVo userMessageVo : userMessageVos) {
			UserMessage userMessage = new UserMessage();
			BeanUtils.copyProperties(userMessageVo,userMessage);
			userMessage.setType(1);
			//把未读的修改成已读
			userMessageService.updateById(userMessage);
			//得到点赞的人的信息
			User userLike = userService.getById(userMessageVo.getUserLikeId());
			LikeVo likeVo = new LikeVo();
			likeVo.setId(userMessageVo.getId());
			likeVo.setNickName(userLike.getNickname());
			likeVo.setAvatar(userLike.getAvatar());
			likeVo.setCreateTime(userMessage.getCreateTime());
			Posts posts = postsService.getById(userMessageVo.getPostsId());
			likeVo.setPostName(posts.getPostsName());
			likeVo.setType(1);
			likeVoList.add(likeVo);
		}
		return likeVoList;
	}

	@Override
	public List<EvaluateMessageVo> queryByLikes(Integer state,String uid) {
		List<UserMessageVo> userMessageVos = userMessageMapper.queryByLike(state,uid);
		List<EvaluateMessageVo> evaluateMessageVos = new ArrayList<>();
		for (UserMessageVo userMessageVo : userMessageVos) {
			UserMessage userMessage = new UserMessage();
			BeanUtils.copyProperties(userMessageVo,userMessage);
			userMessage.setType(1);
			//把未读的修改成已读
			userMessageService.updateById(userMessage);
			//查询是不是评论的消息
			EvaluateMessageVo evaluateMessageVo = new EvaluateMessageVo();
			if(StringUtils.isNotBlank(userMessageVo.getEvaluateId())){
				UserEvaluate userEvaluate = userMessageMapper.queryByEvaluateId(userMessageVo.getEvaluateId());
				if (StringUtils.isNotBlank(userEvaluate.getParentId())) {
					EvaluateMessageVo childEvaluateMessageVo = new EvaluateMessageVo();

					User user = userService.getById(userEvaluate.getUserId());
					childEvaluateMessageVo.setEvaluateName(user.getNickname());
					childEvaluateMessageVo.setAvatar(user.getAvatar());

					//得到评论人的评论消息
					String parentId = userEvaluate.getParentId();
					UserEvaluate parentEvaluate = evaluateService.getById(parentId);
					//得到被评论的名称
					User parentUser = userService.getById(parentEvaluate.getUserId());
					childEvaluateMessageVo.setRespopnseName(parentUser.getNickname());
					//添加评论内容
					childEvaluateMessageVo.setComment(userEvaluate.getContent());
					//添加帖子内容
					Posts posts = postsService.getById(userEvaluate.getPostsId());
					childEvaluateMessageVo.setContent(posts.getContent());
					//添加帖子标题
					childEvaluateMessageVo.setPostsName(posts.getPostsName());
					//添加帖子图片
					String bookUrl = posts.getIntroImg();
					List<String> list = new ArrayList<>();
					list = Arrays.asList(bookUrl.split(","));
					childEvaluateMessageVo.setIntroImg(list);
					childEvaluateMessageVo.setType(1);
					childEvaluateMessageVo.setCreateTime(userMessage.getCreateTime());
					evaluateMessageVos.add(childEvaluateMessageVo);
				} else {
					//得到只有评论的信息
					String userId = userEvaluate.getUserId();
					User user = userService.getById(userId);
					evaluateMessageVo.setEvaluateName(user.getNickname());
					evaluateMessageVo.setAvatar(user.getAvatar());

					if (StringUtils.isBlank(evaluateMessageVo.getRespopnseName())) {
						evaluateMessageVo.setRespopnseName("");
					}
					//添加评论内容
					evaluateMessageVo.setComment(userEvaluate.getContent());
					//添加帖子内容
					Posts posts = postsService.getById(userEvaluate.getPostsId());
					evaluateMessageVo.setContent(posts.getContent());
					//添加帖子标题
					evaluateMessageVo.setPostsName(posts.getPostsName());
					//添加帖子图片
					String bookUrl = posts.getIntroImg();
					List<String> list = new ArrayList<>();
					list = Arrays.asList(bookUrl.split(","));
					evaluateMessageVo.setIntroImg(list);
					evaluateMessageVo.setType(1);
					evaluateMessageVo.setCreateTime(userMessage.getCreateTime());
					evaluateMessageVos.add(evaluateMessageVo);
				}
			}

		}
		return evaluateMessageVos;
	}

	@Override
	public Integer getMessageNum() {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		List<UserMessage> messageNum = userMessageMapper.getMessageNum(user.getId());
		return messageNum.size();
	}

	@Override
	public LikeVo queryNewLike(Integer state, User user) {
		UserMessageVo userMessageVo = userMessageMapper.queryNewLike(state,user.getId());
		/*UserMessage userMessage = new UserMessage();
		BeanUtils.copyProperties(userMessageVo,userMessage);
		userMessage.setType(1);
		//把未读的修改成已读
		userMessageService.updateById(userMessage);*/
		//得到点赞的人的信息
		LikeVo likeVo = new LikeVo();
		if( userMessageVo != null) {
			User userLike = userService.getById(userMessageVo.getUserLikeId());
			if (userLike != null) {
				likeVo.setId(userMessageVo.getId());
				likeVo.setNickName(userLike.getNickname());
				likeVo.setAvatar(userLike.getAvatar());
				Posts posts = postsService.getById(userMessageVo.getPostsId());
				likeVo.setPostName(posts.getPostsName());
				likeVo.setType(userMessageVo.getType());
			}
		}
		return likeVo;
	}

	@Override
	public EvaluateMessageVo queryNewLikes(Integer state, String uid) {
		UserMessageVo userMessageVo = userMessageMapper.queryNewLike(state,uid);
	/*	UserMessage userMessage = new UserMessage();
		BeanUtils.copyProperties(userMessageVo,userMessage);
		userMessage.setType(1);
		//把未读的修改成已读
		userMessageService.updateById(userMessage);*/
		//查询是不是评论的消息
		EvaluateMessageVo evaluateMessageVo = new EvaluateMessageVo();
		if (userMessageVo != null) {
			if (StringUtils.isNotBlank(userMessageVo.getEvaluateId())) {
				UserEvaluate userEvaluate = userMessageMapper.queryByEvaluateId(userMessageVo.getEvaluateId());
				if (StringUtils.isNotBlank(userEvaluate.getParentId())) {
					EvaluateMessageVo childEvaluateMessageVo = new EvaluateMessageVo();

					User user = userService.getById(userEvaluate.getUserId());
					childEvaluateMessageVo.setEvaluateName(user.getNickname());
					childEvaluateMessageVo.setAvatar(user.getAvatar());

					//得到评论人的评论消息
					String parentId = userEvaluate.getParentId();
					UserEvaluate parentEvaluate = evaluateService.getById(parentId);
					//得到被评论的名称
					User parentUser = userService.getById(parentEvaluate.getUserId());
					childEvaluateMessageVo.setRespopnseName(parentUser.getNickname());
					//添加评论内容
					childEvaluateMessageVo.setComment(userEvaluate.getContent());
					//添加帖子内容
					Posts posts = postsService.getById(userEvaluate.getPostsId());
					childEvaluateMessageVo.setContent(posts.getContent());
					//添加帖子标题
					childEvaluateMessageVo.setPostsName(posts.getPostsName());
					//添加帖子图片
					String bookUrl = posts.getIntroImg();
					List<String> list = new ArrayList<>();
					list = Arrays.asList(bookUrl.split(","));
					childEvaluateMessageVo.setIntroImg(list);
					childEvaluateMessageVo.setType(userMessageVo.getType());
					evaluateMessageVo = childEvaluateMessageVo;
				} else {
					//得到只有评论的信息
					String userId = userEvaluate.getUserId();
					User user = userService.getById(userId);
					evaluateMessageVo.setEvaluateName(user.getNickname());
					evaluateMessageVo.setAvatar(user.getAvatar());
					if (StringUtils.isBlank(evaluateMessageVo.getRespopnseName())) {
						evaluateMessageVo.setRespopnseName("");
					}
					//添加评论内容
					evaluateMessageVo.setComment(userEvaluate.getContent());
					//添加帖子内容
					Posts posts = postsService.getById(userEvaluate.getPostsId());
					evaluateMessageVo.setContent(posts.getContent());
					//添加帖子标题
					evaluateMessageVo.setPostsName(posts.getPostsName());
					//添加帖子图片
					String bookUrl = posts.getIntroImg();
					List<String> list = new ArrayList<>();
					list = Arrays.asList(bookUrl.split(","));
					evaluateMessageVo.setIntroImg(list);
					evaluateMessageVo.setType(userMessageVo.getType());
				}
			}
		}


		return evaluateMessageVo;
	}
}
