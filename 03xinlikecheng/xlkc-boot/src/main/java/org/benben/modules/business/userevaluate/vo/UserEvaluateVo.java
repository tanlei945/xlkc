package org.benben.modules.business.userevaluate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.benben.modules.business.userresponse.vo.UserResponseVo;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;


@Data
public class UserEvaluateVo {
	private static final long serialVersionUID = 1L;

	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**评论用户id*/
	@Excel(name = "评论用户id", width = 15)
	private java.lang.String userId;
	/**评论父id*/
	@Excel(name = "评论父id", width = 15)
	private java.lang.String parentId;

	/**昵称*/
	@Excel(name = "昵称", width = 15)
	private java.lang.String nickname;
	/**头像*/
	@Excel(name = "头像", width = 15)
	private java.lang.String avatar;

	/**评论内容*/
	@Excel(name = "评论内容", width = 15)
	private java.lang.Object content;
	/**点赞次数*/
	@Excel(name = "点赞次数", width = 15)
	private java.lang.Integer liknum;
	/**评论数*/
	@Excel(name = "评论数", width = 15)
	private java.lang.Integer evaluateNum;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;

	//这是评论已经评论帖子的人
	private List<UserEvaluateVo> userEvaluateVos;

	/**用户回复表**/
	private List<UserResponseVo> userResponseVos;
}
