package org.benben.modules.business.leavetime.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.leavetime.entity.LeaveTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 用户请假时间表
 * @author： jeecg-boot
 * @date：   2019-07-11
 * @version： V1.0
 */
public interface LeaveTimeMapper extends BaseMapper<LeaveTime> {

	@Select("select * from bb_leave_time where parent_id = #{id}")
	List<LeaveTime> getByParentId(String id);
}
