package org.benben.modules.business.leavetime.service;

import org.benben.modules.business.leavetime.entity.LeaveTime;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 用户请假时间表
 * @author： jeecg-boot
 * @date：   2019-07-11
 * @version： V1.0
 */
public interface ILeaveTimeService extends IService<LeaveTime> {

	List<LeaveTime> getByParentId(String id);
}
