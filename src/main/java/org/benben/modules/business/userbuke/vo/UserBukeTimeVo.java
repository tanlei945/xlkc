package org.benben.modules.business.userbuke.vo;

import lombok.Data;
import org.benben.modules.business.courseassistanttime.vo.TimeVo;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class UserBukeTimeVo {
	/**课程相关联id*/
	@Excel(name = "课程相关联id", width = 15)
	private java.lang.String courseId;
	private java.lang.String listId;
	/**请假内容*/
	@Excel(name = "请假内容", width = 15)
	private java.lang.String comment;
	private List<TimeVo> timeVoList;
}
