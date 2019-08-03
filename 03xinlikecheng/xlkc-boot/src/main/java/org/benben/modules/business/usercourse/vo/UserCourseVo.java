package org.benben.modules.business.usercourse.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class UserCourseVo {

	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
	private java.lang.String courseName;
	@Excel(name = "课程封面地址", width = 15)
	private java.lang.String courseImg;
	/**上课时间*/
	@Excel(name = "上课时间", width = 20, format = "yyyy-MM-dd ")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd ")
	@DateTimeFormat(pattern="yyyy-MM-dd ")
	private java.util.Date starttime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd ")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd ")
	@DateTimeFormat(pattern="yyyy-MM-dd ")
	private java.util.Date endtime;
	/**课程状态 (1.待确认2.已确认*/
	@Excel(name = "课程退款0/可以退款 1/退款中 2/退款完成3/不可退款", width = 15)
	private java.lang.Integer status;

	private  String listId;
}
