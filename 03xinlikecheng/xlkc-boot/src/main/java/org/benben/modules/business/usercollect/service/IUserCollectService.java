package org.benben.modules.business.usercollect.service;

import org.benben.modules.business.usercollect.entity.UserCollect;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.usercollect.vo.UserCollectVo;
import org.benben.modules.business.userposts.entity.Posts;

import java.util.List;

/**
 * @Description: 用户收藏表管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface IUserCollectService extends IService<UserCollect> {

	List<UserCollectVo> queryUserPosts();

	Posts queryByPostsId(String postsId);
}
