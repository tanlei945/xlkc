package org.benben.modules.business.leavetime.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.benben.common.system.query.QueryGenerator;
import org.benben.modules.business.leavetime.entity.LeaveTime;
import org.benben.modules.business.leavetime.mapper.LeaveTimeMapper;
import org.benben.modules.business.leavetime.service.ILeaveTimeService;
import org.benben.modules.business.leavetime.vo.LeaveTimeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 用户请假时间表
 * @author： jeecg-boot
 * @date：   2019-07-11
 * @version： V1.0
 */
@Service
public class LeaveTimeServiceImpl extends ServiceImpl<LeaveTimeMapper, LeaveTime> implements ILeaveTimeService {

	@Autowired
	private LeaveTimeMapper leaveTimeMapper;

	@Override
	public List<LeaveTime> getByParentId(String id) {

		return leaveTimeMapper.getByParentId(id);

	}
}
