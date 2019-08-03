package org.benben.modules.business.userleave.entity;

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
 * @Description: 用户请假
 * @author： jeecg-boot
 * @date：   2019-06-03
 * @version： V1.0
 */
@Data
@TableName("user_leave")
public class UserLeave implements Serializable {
	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**课程相关联id*/
	@Excel(name = "课程相关联id", width = 15)
	private java.lang.String courseId;
	/**userId*/
	@Excel(name = "userId", width = 15)
	private java.lang.String userId;
	/**请假内容*/
	@Excel(name = "请假内容", width = 15)
	private java.lang.String comment;
	/**0/可以请假 1/审核中 2/成功*/
	@Excel(name = "0/可以请假 1/审核中 2/成功", width = 15)
	private java.lang.Integer status;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	private java.lang.String updateBy;
}
