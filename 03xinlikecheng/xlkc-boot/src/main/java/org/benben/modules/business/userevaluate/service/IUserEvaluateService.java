package org.benben.modules.business.userevaluate.service;

import org.benben.modules.business.userevaluate.entity.UserEvaluate;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.userevaluate.vo.UserEvaluateVo;

import java.util.List;

/**
 * @Description: 用户评论表管理
 * @author： jeecg-boot
 * @date：   2019-06-11
 * @version： V1.0
 */
public interface IUserEvaluateService extends IService<UserEvaluate> {

	List<UserEvaluateVo> queryByPostId(String postId);

	List<UserEvaluateVo> queryByEvaluateId(String evaluateId);
}
