package org.benben.modules.business.coursetimerelevan.entity;

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
 * @Description: 课程时间关系表
 * @author： jeecg-boot
 * @date：   2019-06-21
 * @version： V1.0
 */
@Data
@TableName("bb_course_time_relevan")
public class CourseTimeRelevan implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**课程相关id*/
	@Excel(name = "课程相关id", width = 15)
	private java.lang.String cid;
	/**课程时间相关id*/
	@Excel(name = "课程时间相关id", width = 15)
	private java.lang.String ctid;
	/**creatTime*/
	@Excel(name = "creatTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date creatTime;
	/**creatBy*/
	@Excel(name = "creatBy", width = 15)
	private java.lang.String creatBy;
	/**updateTime*/
	@Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**updateBy*/
	@Excel(name = "updateBy", width = 15)
	private java.lang.String updateBy;
}
