package org.benben.modules.business.userevaluate.service.impl;

import org.apache.commons.lang.StringUtils;
import org.benben.modules.business.recommendposts.mapper.RecommendPostsMapper;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.userevaluate.entity.UserEvaluate;
import org.benben.modules.business.userevaluate.mapper.UserEvaluateMapper;
import org.benben.modules.business.userevaluate.service.IUserEvaluateService;
import org.benben.modules.business.userevaluate.vo.UserEvaluateVo;
import org.benben.modules.business.userresponse.entity.UserResponse;
import org.benben.modules.business.userresponse.mapper.UserResponseMapper;
import org.benben.modules.business.userresponse.vo.UserResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 用户评论表管理
 * @author： jeecg-boot
 * @date：   2019-06-11
 * @version： V1.0
 */
@Service
public class UserEvaluateServiceImpl extends ServiceImpl<UserEvaluateMapper, UserEvaluate> implements IUserEvaluateService {

	@Autowired
	private UserEvaluateMapper userEvaluateMapper;

	@Autowired
	private IUserEvaluateService userEvaluateService;

	@Autowired
	private UserResponseMapper userResponseMapper;



	@Override
	public List<UserEvaluateVo> queryByPostId(String postId) {
		List<UserEvaluateVo> userEvaluateVo = userEvaluateMapper.queryByPostId(postId);

		for (int i = 0; i < userEvaluateVo.size(); i++) {
			if (StringUtils.isBlank(userEvaluateVo.get(i).getParentId()) ) {
				//搜索是否有评论的人
				List<UserEvaluateVo> userEvaluateVos = userEvaluateMapper.queryById(userEvaluateVo.get(i).getId(),postId);
				if (userEvaluateVos.size() > 5) {
					for (int s = 5 ; s < userEvaluateVos.size(); s++) {
						userEvaluateVos.remove(s);
						s = 5;
					}
				}

				if (userEvaluateVos.size() != 0 ) {
					int q = 0;
					for (UserEvaluateVo vo : userEvaluateVos) {
						User user1 = userEvaluateMapper.queryUserId(vo.getUserId());
						if (user1 != null) {
							vo.setNickname(user1.getNickname());
							vo.setAvatar(user1.getAvatar());
						}



						//查看回复的人
						List<UserResponseVo> userResponseVos = userResponseMapper.queryByEvaluateId(userEvaluateVos.get(q).getId());
						List<UserResponse> userResponses = userResponseMapper.queryByEvaluateId1(userEvaluateVos.get(q).getId());
						for (int j = 0 ; j < userResponses.size(); j++) {
							//得到评论人和回复人昵称

							String evaluateName = user1.getNickname();
							String responseName = userResponseMapper.queryById(userResponses.get(j).getUserId());
							userResponseVos.get(j).setEvaluateUserName(evaluateName);
							userResponseVos.get(j).setUserName(responseName);
							//假如是楼主则是 true
							userResponseVos.get(j).setHost(true);
						}
						//添加到回复楼层的集合
						vo.setUserResponseVos(userResponseVos);
						q++;

					}
					//添加到评论楼主的集合
					userEvaluateVo.get(i).setUserEvaluateVos(userEvaluateVos);
					//查询评论数和点赞次数
					UserEvaluate userEvaluate = new UserEvaluate();
					System.out.println(" eva   "+ userEvaluateVo.get(i).getId());
					Integer likeNum = userEvaluateMapper.getLikeNum(userEvaluateVo.get(i).getId());
					System.out.println(likeNum);
//					Integer evaluateNum = userEvaluateMapper.getEvaluateNum(userEvaluateVos.get(q).getId());
					Integer evaluateNum = userEvaluateVos.size();
					userEvaluate.setLiknum(likeNum);
					userEvaluate.setEvaluateNum(evaluateNum);
					userEvaluate.setId(userEvaluateVo.get(i).getId());
					userEvaluateService.updateById(userEvaluate);
					System.out.println( "数量"+ evaluateNum);
					//得到评论数和点赞数
					userEvaluateVo.get(i).setLiknum(likeNum);

					userEvaluateVo.get(i).setEvaluateNum(evaluateNum);



				} else {
					//查询评论数和点赞次数
					UserEvaluate userEvaluate = new UserEvaluate();
					System.out.println(" eva   "+ userEvaluateVo.get(i).getId());
					Integer likeNum = userEvaluateMapper.getLikeNum(userEvaluateVo.get(i).getId());
					System.out.println(likeNum);
					//					Integer evaluateNum = userEvaluateMapper.getEvaluateNum(userEvaluateVos.get(q).getId());
					Integer evaluateNum = userEvaluateVos.size();
					userEvaluate.setLiknum(likeNum);
					userEvaluate.setEvaluateNum(evaluateNum);
					userEvaluate.setId(userEvaluateVo.get(i).getId());
					userEvaluateService.updateById(userEvaluate);
					System.out.println( "数量"+ evaluateNum);
					//得到评论数和点赞数
					userEvaluateVo.get(i).setLiknum(likeNum);

					userEvaluateVo.get(i).setEvaluateNum(evaluateNum);

				}

				User user = userEvaluateMapper.queryUserId(userEvaluateVo.get(i).getUserId());
				if (user != null) {
					userEvaluateVo.get(i).setAvatar(user.getAvatar());
					userEvaluateVo.get(i).setNickname(user.getNickname());
				}

			} else  {
				userEvaluateVo.remove(i);
				System.out.println(3333);
				i = i-1;
			}
		}

		for (UserEvaluateVo evaluateVo : userEvaluateVo) {
			if (evaluateVo.getUserEvaluateVos() == null) {
				List<UserEvaluateVo> u = new ArrayList<>();
				evaluateVo.setUserEvaluateVos(u);
			}
			if (evaluateVo.getUserResponseVos() == null) {
				List<UserResponseVo> r = new ArrayList<>();
				evaluateVo.setUserResponseVos(r);
			}
		}
		return userEvaluateVo;
	}

	@Override
	public List<UserEvaluateVo> queryByEvaluateId(String evaluateId) {
		UserEvaluate userEvaluate = userEvaluateService.getById(evaluateId);
		UserEvaluateVo userEvaluateVo = new UserEvaluateVo();
		BeanUtils.copyProperties(userEvaluate,userEvaluateVo);
		//搜索是否有评论的人
		List<UserEvaluateVo> userEvaluateVos = userEvaluateMapper.queryById(userEvaluate.getId(),userEvaluate.getPostsId());

		if (userEvaluateVos.size() != 0 ) {
			int q = 0;
			for (UserEvaluateVo vo : userEvaluateVos) {
				User user1 = userEvaluateMapper.queryUserId(vo.getUserId());
				if (user1 != null) {
					vo.setNickname(user1.getNickname());
					vo.setAvatar(user1.getAvatar());
					vo.setAvatar(user1.getAvatar());
				}
				//查询点赞数
				UserEvaluate userEvaluate1 = new UserEvaluate();
				Integer likeNum = userEvaluateMapper.getLikeNum(userEvaluateVo.getId());
				Integer evaluateNum = userEvaluateMapper.getEvaluateNum(userEvaluateVo.getId());
				userEvaluate.setLiknum(likeNum);
				userEvaluate.setEvaluateNum(evaluateNum);
				userEvaluate.setId(userEvaluateVo.getId());
				userEvaluateService.updateById(userEvaluate);

				//得到点赞数
				vo.setLiknum(likeNum);
				//得到回复数
				//查看回复的人
				List<UserResponseVo> userResponseVos = userResponseMapper.queryByEvaluateId(userEvaluateVos.get(q).getId());
				List<UserResponse> userResponses = userResponseMapper.queryByEvaluateId1(userEvaluateVos.get(q).getId());
				for (int j = 0 ; j < userResponses.size(); j++) {
					//得到评论人和回复人昵称

//					System.out.println(userResponses.get(i).getEvaluateUserId());
					String evaluateName = user1.getNickname();
					String responseName = userResponseMapper.queryById(userResponses.get(j).getUserId());
					userResponseVos.get(j).setEvaluateUserName(evaluateName);
					userResponseVos.get(j).setUserName(responseName);
					//假如是楼主则是 true
					userResponseVos.get(j).setHost(true);
				}
				//添加到回复楼层的集合
				vo.setUserResponseVos(userResponseVos);
				vo.setEvaluateNum(userEvaluateVos.size());
				q++;
			}
			//添加到评论楼主的集合
			userEvaluateVo.setUserEvaluateVos(userEvaluateVos);

		}
		return userEvaluateVos;
	}
}
