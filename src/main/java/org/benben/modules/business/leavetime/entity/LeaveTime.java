package org.benben.modules.business.leavetime.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 用户请假时间表
 * @author： jeecg-boot
 * @date：   2019-07-11
 * @version： V1.0
 */
@Data
@TableName("bb_leave_time")
public class LeaveTime implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String uid;
	//对应的用户请假表id
	private java.lang.String userLeaveId;
	/**用户请假月*/
	@Excel(name = "用户请假月", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date date;
	/**parentId*/
	@Excel(name = "parentId", width = 15)
	private java.lang.String parentId;
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

	//用户请假内容
	@TableField(exist = false)
	private java.lang.String comment;
	//用户请假状态
	@TableField(exist = false)
	private java.lang.Integer status;

	@TableField(exist = false)
	private java.lang.String nickName;

	@TableField(exist = false)
	private java.lang.String days;

}
