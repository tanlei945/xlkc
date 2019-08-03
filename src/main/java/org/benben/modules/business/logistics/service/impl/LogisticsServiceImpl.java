package org.benben.modules.business.logistics.service.impl;

import org.benben.modules.business.logistics.entity.Logistics;
import org.benben.modules.business.logistics.mapper.LogisticsMapper;
import org.benben.modules.business.logistics.service.ILogisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 物流信息管理
 * @author： jeecg-boot
 * @date：   2019-06-11
 * @version： V1.0
 */
@Service
public class LogisticsServiceImpl extends ServiceImpl<LogisticsMapper, Logistics> implements ILogisticsService {

	@Autowired
	private LogisticsMapper logisticsMapper;

	@Override
	public Logistics queryById(String ordernumber) {
		return logisticsMapper.queryById(ordernumber);
	}

	@Override
	public Logistics queryByLogistics(String orderId) {
		return logisticsMapper.queryById(orderId);
	}
}
