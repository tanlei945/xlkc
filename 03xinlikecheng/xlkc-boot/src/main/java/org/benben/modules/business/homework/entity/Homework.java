package org.benben.modules.business.homework.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 管理功课
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Data
@TableName("bb_homework")
public class Homework implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**userId*/
	@Excel(name = "userId", width = 15)
	private java.lang.String userId;
	/**课程相关联id*/
	@Excel(name = "课程相关联id", width = 15)
	private java.lang.String courseId;
	@TableField(exist = false)
	private java.lang.String courseName;
	/**小组名称*/
	@Excel(name = "小组名称", width = 15)
	private java.lang.String groupName;
	/**组员*/
	@Excel(name = "组员", width = 15)
	private java.lang.String crew;

	@Excel(name = "语种", width = 15)
	private java.lang.Integer language;
	/**文档路径*/
	@Excel(name = "文档路径", width = 15)
	private java.lang.String wordUrl;
	/**文档名称*/
	@Excel(name = "文档名称", width = 15)
	private java.lang.String wordName;
	/**图片路径*/
	@Excel(name = "图片路径", width = 15)
	private java.lang.String pictureUrl;
	/**图片名称*/
	@Excel(name = "图片名称", width = 15)
	private java.lang.String pictureName;
	/**音频路径*/
	@Excel(name = "音频路径", width = 15)
	private java.lang.String voiceUrl;
	/**音频名称*/
	@Excel(name = "音频名称", width = 15)
	private java.lang.String voiceName;
	/**视频路径*/
	@Excel(name = "视频路径", width = 15)
	private java.lang.String videoUrl;
	/**视频名称*/
	@Excel(name = "视频名称", width = 15)
	private java.lang.String videoName;
	@TableField(exist = false)
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date courseTime;
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
