package org.benben.modules.business.buketime.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.benben.modules.business.coursetime.vo.DayTime;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class UserBukeDetailsVo {

	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
	private java.lang.String courseName;
	@Excel(name = "课程名称", width = 15)
	private java.lang.String courseImg;
	@Excel(name = "请假内容", width = 15)
	private java.lang.String comment;

	private java.lang.Integer bukeNum;

	/**上课时间*/
	@Excel(name = "上课时间", width = 20, format = "yyyy-MM-dd ")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd ")
	@DateTimeFormat(pattern = "yyyy-MM-dd ")
	private java.util.Date starttime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd ")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd ")
	@DateTimeFormat(pattern = "yyyy-MM-dd ")
	private java.util.Date endtime;

	@Excel(name = "courseTime", width = 15)
	private List<DayTime> courseTimes;
}
