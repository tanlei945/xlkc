package org.benben.modules.business.usermessage.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.benben.common.menu.ResultEnum;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class EvaluateMessageVo {
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;

	private java.lang.String evaluateName;

	private String avatar;

	private java.lang.String respopnseName;

	@Excel(name = "帖子内容", width = 15)
	private java.lang.String content;

	@Excel(name = "评论内容", width = 15)
	private java.lang.String comment;

	@Excel(name = "帖子简介轮播图", width = 15)
	private List<String> introImg;
	/**帖子标题*/
	@Excel(name = "帖子标题", width = 15)
	private java.lang.String postsName;

	//0/是未读，1/是已读
	private java.lang.Integer type;

	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;

}
