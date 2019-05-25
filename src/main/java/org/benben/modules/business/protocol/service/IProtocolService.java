package org.benben.modules.business.protocol.service;

import org.benben.modules.business.protocol.entity.Protocol;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 协议管理
 * @author： jeecg-boot
 * @date：   2019-05-25
 * @version： V1.0
 */
public interface IProtocolService extends IService<Protocol> {

	Protocol queryByType(Integer type);

}
