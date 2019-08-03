package org.benben.modules.business.recommendposts.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class RecommendsVos {
	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**帖子名称*/
	@Excel(name = "帖子名称", width = 15)
	private java.lang.String name;
	/**帖子内容*/
	@Excel(name = "帖子内容", width = 15)
	private java.lang.String content;
	/**点赞次数*/
	@Excel(name = "点赞次数", width = 15)
	private java.lang.Integer likeNum;
	/**评论次数*/
	@Excel(name = "评论次数", width = 15)
	private java.lang.Integer commentNum;
	/**评论次数*/
	@Excel(name = "1/推荐帖子，0/挑战帖子", width = 15)
	private java.lang.Integer type;
	/**帖子简介*/
	@Excel(name = "帖子简介", width = 15)
	private java.lang.String intro;
	/**帖子简介轮播图*/
	@Excel(name = "帖子简介轮播图", width = 15)
	private java.util.List<String> introImg;
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

	@TableField( exist = false)
	private java.lang.Integer collectType;
	@TableField( exist = false)
	private java.lang.Integer likeType;
}
