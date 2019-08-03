package org.benben.modules.business.usermessage.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class UserMessageVo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**昵称*/
	@Excel(name = "昵称", width = 15)
	private java.lang.String nickname;
	/**点赞用户id*/
	@Excel(name = "点赞用户姓名id", width = 15)
	private java.lang.String userLikeId;
	/**用户id*/
	@Excel(name = "用户名称id", width = 15)
	private java.lang.String userId;
	/**评论id*/
	@Excel(name = "评论id", width = 15)
	private java.lang.String evaluateId;
	/**回复id*/
	@Excel(name = "回复id", width = 15)
	private java.lang.String responseId;
	/**和帖子相关联id*/
	@Excel(name = "和帖子相关联id", width = 15)
	private java.lang.String postsId;
	/**0/赞同的消息 1/评论的消息 2/普通消息*/
	@Excel(name = "0/赞同的消息 1/评论的消息 2/普通消息", width = 15)
	private java.lang.Integer state;
	/**消息的描述*/
	@Excel(name = "消息的描述", width = 15)
	private java.lang.String comment;
	//0/是未读，1/是已读
	private java.lang.Integer type;
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
