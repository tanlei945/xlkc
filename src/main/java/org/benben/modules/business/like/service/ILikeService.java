package org.benben.modules.business.like.service;

import org.benben.modules.business.like.entity.Like;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 点赞管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
public interface ILikeService extends IService<Like> {

	Integer getLikeType(String id, String id1);

	List<Like> queryLike(String id, String postId);

	List<Like> queryEvaluate(String id, String evaluateId);

}
