package org.benben.modules.business.usercollect.service.impl;

import javafx.geometry.Pos;
import org.benben.modules.business.usercollect.entity.UserCollect;
import org.benben.modules.business.usercollect.mapper.UserCollectMapper;
import org.benben.modules.business.usercollect.service.IUserCollectService;
import org.benben.modules.business.usercollect.vo.UserCollectVo;
import org.benben.modules.business.userposts.entity.Posts;
import org.benben.modules.business.userposts.vo.UserPostsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 我的收藏表
 * @author： jeecg-boot
 * @date： 2019-05-22
 * @version： V1.0
 */
@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements IUserCollectService {

	@Autowired
	private UserCollectMapper collectMapper;

	@Override
	public List<UserCollectVo> queryUserPosts() {
		List<UserCollectVo> userCollectVos = collectMapper.querrUserPosts();
		return userCollectVos;
	}

	@Override
	public Posts queryByPostsId(String postsId) {
		Posts posts = collectMapper.queryByPostsId(postsId);
		return posts;
	}
}
