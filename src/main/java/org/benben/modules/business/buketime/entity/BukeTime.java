package org.benben.modules.business.buketime.entity;

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
 * @Description: 补课时间管理
 * @author： jeecg-boot
 * @date：   2019-07-18
 * @version： V1.0
 */
@Data
@TableName("bb_buke_time")
public class BukeTime implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String uid;
	/**用户补课id*/
	@Excel(name = "用户补课id", width = 15)
	private java.lang.String userBukeId;
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

	@TableField(exist = false)
	private java.lang.String comment;
	@TableField(exist = false)
	private Integer status;
	@TableField(exist = false)
	private java.lang.String nickName;

	@TableField(exist = false)
	private java.lang.String days;
}
