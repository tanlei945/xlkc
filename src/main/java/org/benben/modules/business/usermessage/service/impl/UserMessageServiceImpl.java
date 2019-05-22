package org.benben.modules.business.usermessage.service.impl;

import org.benben.modules.business.usermessage.entity.UserMessage;
import org.benben.modules.business.usermessage.mapper.UserMessageMapper;
import org.benben.modules.business.usermessage.service.IUserMessageService;
import org.benben.modules.business.usermessage.vo.UserMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 用户消息表
 * @author： jeecg-boot
 * @date：   2019-05-20
 * @version： V1.0
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements IUserMessageService {

	@Autowired
	private UserMessageMapper userMessageMapper;

	@Override
	public List<UserMessage> queryByState(Integer state) {
		List<UserMessage> list = userMessageMapper.queryByState(state);
		return list;
	}

	@Override
	public List<UserMessageVo> queryByLike(Integer state) {
		List<UserMessageVo> userMessageVos = userMessageMapper.queryByLike(state);
		return userMessageVos;
	}
}
