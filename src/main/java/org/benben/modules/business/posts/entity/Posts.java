package org.benben.modules.business.posts.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 帖子的管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
@Data
@TableName("bb_posts")
public class Posts implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**课程分类id*/
	@Excel(name = "课程分类id", width = 15)
	private java.lang.String courseTypeId;
	/**帖子标题*/
	@Excel(name = "课程分类名称", width = 15)
	private java.lang.String courseTypeName;
	/**帖子标题*/
	@Excel(name = "帖子标题", width = 15)
	private java.lang.String postsName;
	/**帖子内容*/
	@Excel(name = "帖子内容", width = 15)
	private java.lang.String content;
	/**图片地址*/
	@Excel(name = "图片地址", width = 15)
	private java.lang.String pictureUrl;
	/**点赞次数*/
	@Excel(name = "点赞次数", width = 15)
	private java.lang.Integer liknum;
	/**评论数*/
	@Excel(name = "评论数", width = 15)
	private java.lang.Integer commentNum;
	/**搜索次数*/
	@Excel(name = "搜索次数", width = 15)
	private java.lang.Integer searchNum;
	/**帖子简介*/
	@Excel(name = "帖子简介", width = 15)
	private java.lang.String intro;
	/**帖子简介轮播图*/
	@Excel(name = "帖子简介轮播图", width = 15)
	private java.lang.String introImg;
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
