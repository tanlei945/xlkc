package org.benben.modules.business.userevaluate.entity;

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
 * @Description: 用户评论表管理
 * @author： jeecg-boot
 * @date：   2019-06-11
 * @version： V1.0
 */
@Data
@TableName("user_evaluate")
public class UserEvaluate implements Serializable {

	public UserEvaluate() {
	}

	public UserEvaluate( String userId, String parentId, String postsId, String content) {
		this.userId = userId;
		this.parentId = parentId;
		this.postsId = postsId;
		this.content = content;
	}

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
	/**帖子相关联iid*/
	@Excel(name = "帖子相关联id", width = 15)
	private java.lang.String postsId;
	/**评论内容*/
	@Excel(name = "评论内容", width = 15)
	private java.lang.String content;
	/**点赞次数*/
	@Excel(name = "点赞次数", width = 15)
	private java.lang.Integer liknum;
	/**评论数*/
	@Excel(name = "评论数", width = 15)
	private java.lang.Integer evaluateNum;
	/**0：评论商家 1：评论骑手*/
	@Excel(name = "0：评论商家 1：评论骑手", width = 15)
	private java.lang.String evaluateType;
	/**用户上传图片*/
	@Excel(name = "用户上传图片", width = 15)
	private java.lang.String imgUrl;
	/**是否被删除 0：已删除 1：未删除*/
	@Excel(name = "是否被删除 0：已删除 1：未删除", width = 15)
	private java.lang.String delFlag;
	/**创建者*/
	@Excel(name = "创建者", width = 15)
	private java.lang.String createBy;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**更新人*/
	@Excel(name = "更新人", width = 15)
	private java.lang.String updateBy;
	/**更新时间*/
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
}
