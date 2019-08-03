package org.benben.modules.business.recommendposts.service;

import org.benben.modules.business.recommendposts.entity.RecommendPosts;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.recommendposts.vo.RecommendPostsVo;

/**
 * @Description: 管理员推荐帖子
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
public interface IRecommendPostsService extends IService<RecommendPosts> {

	RecommendPostsVo listRecommendPosts(Integer pageNumber, Integer pageSize);

	RecommendPosts challengePosts();

}
