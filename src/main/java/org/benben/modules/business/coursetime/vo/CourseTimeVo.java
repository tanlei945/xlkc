package org.benben.modules.business.coursetime.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class CourseTimeVo {
	private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
	private java.lang.String courseName;
	@Excel(name = "课程封面地址", width = 15)
	private java.lang.String courseImg;
	@Excel(name = "上课时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date starttime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date endtime;

	/**每一节课的时间*/

	private List<DayTime> dates;


}
