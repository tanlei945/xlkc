package org.benben.modules.business.coursetime.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class CourseTimes {
	private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**课程开始时间*/
	@Excel(name = "课程开始时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date date;
	/**那一节课*/
	@Excel(name = "那一节课", width = 15)
	private java.lang.Integer chapter;
	/**那一节课*/
	@Excel(name = "父id", width = 15)
	private java.lang.String parentId;
	/**那一节课*/
	@Excel(name = "对应的天数 日", width = 15)
	private java.lang.Integer day;
	/**creatTime*/
	@Excel(name = "creatTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**creatBy*/
	@Excel(name = "createBy", width = 15)
	private java.lang.String createBy;
	/**updateTime*/
	@Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**updateBy*/
	@Excel(name = "updateBy", width = 15)
	private java.lang.String updateBy;
}
