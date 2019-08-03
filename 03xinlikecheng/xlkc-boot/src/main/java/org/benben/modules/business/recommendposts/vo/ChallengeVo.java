package org.benben.modules.business.recommendposts.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ChallengeVo {

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	@Excel(name = "帖子名称", width = 15)
	private java.lang.String name;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**评论次数*/
	@Excel(name = "评论次数", width = 15)
	private java.lang.Integer commentNum;
	/**点赞次数*/
	@Excel(name = "点赞次数", width = 15)
	private java.lang.Integer likeNum;
	@TableField( exist = false)
	private java.lang.Integer collectType;
	@TableField( exist = false)
	private java.lang.Integer likeType;
}
