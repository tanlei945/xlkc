package org.benben.modules.business.userfeedback.service.impl;

import org.benben.modules.business.userfeedback.entity.UserFeedback;
import org.benben.modules.business.userfeedback.mapper.UserFeedbackMapper;
import org.benben.modules.business.userfeedback.service.IUserFeedbackService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户反馈表
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
@Service
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback> implements IUserFeedbackService {

}
