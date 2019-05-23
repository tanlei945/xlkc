package org.benben.modules.business.userfeedback.entity;

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
 * @Description: 用户反馈表
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Data
@TableName("user_feedback")
public class UserFeedback implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户相关联id*/
	@Excel(name = "用户相关联id", width = 15)
	private java.lang.String uid;
	/**问题类型 1下载/加载问题 2/付费问题 3/课程问题 4/体验问题*/
	@Excel(name = "问题类型 1下载/加载问题 2/付费问题 3/课程问题 4/体验问题", width = 15)
	private java.lang.String questionType;
	/**反馈描述*/
	@Excel(name = "反馈描述", width = 15)
	private java.lang.String comment;
	/**反馈图片*/
	@Excel(name = "反馈图片", width = 15)
	private java.lang.String pickerUrl;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
	private java.lang.String contact;
	/**创建人时间*/
	@Excel(name = "创建人时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
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
