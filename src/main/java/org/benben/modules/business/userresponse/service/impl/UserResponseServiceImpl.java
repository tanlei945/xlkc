package org.benben.modules.business.userresponse.service.impl;

import org.benben.modules.business.userresponse.entity.UserResponse;
import org.benben.modules.business.userresponse.mapper.UserResponseMapper;
import org.benben.modules.business.userresponse.service.IUserResponseService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户反馈表管理
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
@Service
public class UserResponseServiceImpl extends ServiceImpl<UserResponseMapper, UserResponse> implements IUserResponseService {

}
