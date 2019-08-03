package org.benben.modules.business.like.service.impl;

import org.benben.modules.business.like.entity.Like;
import org.benben.modules.business.like.mapper.LikeMapper;
import org.benben.modules.business.like.service.ILikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 点赞管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements ILikeService {

	@Autowired
	private LikeMapper likeMapper;

	@Override
	public Integer getLikeType(String uid, String postId) {
		return likeMapper.getLikeType(uid,postId);
	}

	@Override
	public List<Like> queryLike(String id, String postId) {
		return likeMapper.queryLike(id,postId);
	}

	@Override
	public List<Like> queryEvaluate(String id, String evaluateId) {
		return likeMapper.queryEvaluate(id,evaluateId);
	}
}
