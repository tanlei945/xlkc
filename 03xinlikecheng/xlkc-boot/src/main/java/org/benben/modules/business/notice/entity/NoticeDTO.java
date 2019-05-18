package org.benben.modules.business.notice.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 用户通知
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@Data
@TableName("user_message")
@ApiModel("通知实体")
public class NoticeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键id*/
	@TableId(type = IdType.UUID)
	@ApiModelProperty("主键")
	private java.lang.Integer id;
	/**和帖子相关联id*/
	@Excel(name = "和帖子相关联id", width = 15)
	@ApiModelProperty("和帖子相关联id")
	private java.lang.Integer postsId;
	/**消息名称*/
	@Excel(name = "消息名称", width = 15)
	@ApiModelProperty("消息名称")
	private java.lang.String name;
	/**消息的描述*/
	@Excel(name = "消息的描述", width = 15)
	@ApiModelProperty("消息的描述")
	private java.lang.String comment;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("创建时间")
	private java.util.Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.Integer createBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty("修改时间")
	private java.util.Date updateTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	private java.lang.Integer updateBy;
}
