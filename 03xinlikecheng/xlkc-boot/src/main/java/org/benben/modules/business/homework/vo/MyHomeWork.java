package org.benben.modules.business.homework.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class MyHomeWork {
	/**课程相关联id*/
	@Excel(name = "课程相关联id", width = 15)
	private java.lang.String id;

	@Excel(name = "功课id", width = 15)
	private java.lang.String homeworkId;

	/**小组名称*/
	@Excel(name = "课程名称", width = 15)
	private java.lang.String courseName;

	@Excel(name = "课程封面地址", width = 15)
	private java.lang.String courseImg;

	/**上课时间*/
	@Excel(name = "上课时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date starttime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date endtime;
}
