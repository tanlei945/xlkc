package org.benben.modules.business.usermessage.service;

import org.benben.modules.business.usermessage.entity.UserMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.usermessage.vo.UserMessageVo;

import java.util.List;

/**
 * @Description: 用户消息管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface IUserMessageService extends IService<UserMessage> {

	List<UserMessage> queryByState(Integer state);

	List<UserMessageVo> queryByLike(Integer state);
}
