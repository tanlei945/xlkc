package org.benben.modules.business.courseassistanttime.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 助教的时间
 * @author： jeecg-boot
 * @date：   2019-07-10
 * @version： V1.0
 */
@Data
@TableName("bb_course_assistant_time")
public class CourseAssistanTime implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String uid;
	/**课程开始时间*/
	@Excel(name = "课程开始时间", width = 15, format = "yyyy-MM")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM")
    @DateTimeFormat(pattern="yyyy-MM")
	private java.util.Date date;
	/**parentId*/
	@Excel(name = "parentId", width = 15)
	private java.lang.String parentId;
	/**课程相关联id*/
	@Excel(name = "课程助教时间id", width = 15)
	private java.lang.String courseAssistantId;
	/**对应的日期天数*/
	@Excel(name = "对应的日期天数", width = 15)
	private java.lang.Integer day;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**createBy*/
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
