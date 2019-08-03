package org.benben.modules.business.userleave.vo;

import lombok.Data;
import org.benben.modules.business.usercourse.vo.UserCourseVo;
import org.benben.modules.business.userleave.entity.UserLeave;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class UserLeavesVo {
	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "userLeaveVos", width = 15)
	private List<UserCourseStateVo> userCourseStateVos;

}
