package org.benben.modules.business.protocol.service.impl;

import org.benben.modules.business.protocol.entity.Protocol;
import org.benben.modules.business.protocol.mapper.ProtocolMapper;
import org.benben.modules.business.protocol.service.IProtocolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 协议管理
 * @author： jeecg-boot
 * @date：   2019-05-25
 * @version： V1.0
 */
@Service
public class ProtocolServiceImpl extends ServiceImpl<ProtocolMapper, Protocol> implements IProtocolService {

	@Autowired
	private ProtocolMapper protocolMapper;

	@Override
	public Protocol queryByType(Integer type) {
		return protocolMapper.queryByType(type);
	}
}
