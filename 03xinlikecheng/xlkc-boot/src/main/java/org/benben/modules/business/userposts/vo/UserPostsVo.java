package org.benben.modules.business.userposts.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class UserPostsVo {
	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**和帖子相关联id*/
	@Excel(name = "和帖子相关联id", width = 15)
	private java.lang.String postsId;
	/**帖子标题*/
	@Excel(name = "帖子标题", width = 15)
	private java.lang.String postsName;
	/**帖子内容*/
	@Excel(name = "帖子内容", width = 15)
	private java.lang.String comment;
	/**图片地址*/
	@Excel(name = "图片地址", width = 15)
	private java.lang.String pictureUrl;
	/**点赞次数*/
	@Excel(name = "点赞次数", width = 15)
	private java.lang.Integer liknum;
	/**评论数*/
	@Excel(name = "评论数", width = 15)
	private java.lang.Integer commentNum;
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
	private java.lang.String uodateBy;
}
