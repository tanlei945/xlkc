package org.benben.modules.business.userleave.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class UserCourseStateVo {

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	private java.lang.String listId;

	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
	private java.lang.String courseName;
	@Excel(name = "课程封面地址", width = 15)
	private java.lang.String courseImg;
	/**上课时间*/
	@Excel(name = "上课时间", width = 20, format = "yyyy-MM-dd ")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd ")
	@DateTimeFormat(pattern = "yyyy-MM-dd ")
	private java.util.Date starttime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd ")
	@JsonFormat(timezone = "GMT+8 ", pattern = "yyyy-MM-dd ")
	@DateTimeFormat(pattern = "yyyy-MM-dd ")
	private java.util.Date endtime;

	/**0/可以请假 1/审核中 2/成功*/
	@Excel(name = "0/可以请假 1/审核中 2/成功", width = 15)
	private java.lang.Integer status;
}
