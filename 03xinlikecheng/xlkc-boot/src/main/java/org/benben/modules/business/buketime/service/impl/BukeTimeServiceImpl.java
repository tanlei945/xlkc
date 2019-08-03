package org.benben.modules.business.buketime.service.impl;

import org.benben.modules.business.buketime.entity.BukeTime;
import org.benben.modules.business.buketime.mapper.BukeTimeMapper;
import org.benben.modules.business.buketime.service.IBukeTimeService;
import org.benben.modules.business.buketime.vo.UserBukeDetailsVo;
import org.benben.modules.business.coursetime.vo.Day;
import org.benben.modules.business.coursetime.vo.DayTime;
import org.benben.modules.business.userleave.vo.UserLeaveDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 补课时间管理
 * @author： jeecg-boot
 * @date：   2019-07-18
 * @version： V1.0
 */
@Service
public class BukeTimeServiceImpl extends ServiceImpl<BukeTimeMapper, BukeTime> implements IBukeTimeService {

	@Autowired
	private BukeTimeMapper bukeTimeMapper;

	@Override
	public UserBukeDetailsVo getUserBukeDetails(String id, String bukeId) {
		List<DayTime> courseTime = bukeTimeMapper.getTime(id,bukeId);
		for (DayTime time : courseTime) {
			List<Day> day = bukeTimeMapper.getDay(time.getId());
			time.setDays(day);
		}
		UserBukeDetailsVo userBukeDetailsVo = new UserBukeDetailsVo();
		userBukeDetailsVo.setCourseTimes(courseTime);
		return userBukeDetailsVo;
	}

	@Override
	public List<BukeTime> getByParentId(String parentId) {
		return bukeTimeMapper.getByParentId(parentId);
	}
}
