package org.benben.modules.business.userbuke.entity;

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
 * @Description: 用户补课的管理
 * @author： jeecg-boot
 * @date：   2019-06-05
 * @version： V1.0
 */
@Data
@TableName("user_buke")
public class UserBuke implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户相关联id*/
	@Excel(name = "用户相关联id", width = 15)
	private java.lang.String uid;
	/**课程相关联id*/
	@Excel(name = "课程相关联id", width = 15)
	private java.lang.String courseId;
	/**补课内容*/
	@Excel(name = "补课内容", width = 15)
	private java.lang.String comment;
	/**0/可以补课 1/审核中 2/成功*/
	@Excel(name = "0/可以补课 1/审核中 2/成功", width = 15)
	private java.lang.Integer status;
	@Excel(name = "补课剩余人数", width = 15)
	private java.lang.Integer bukeNum;
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
