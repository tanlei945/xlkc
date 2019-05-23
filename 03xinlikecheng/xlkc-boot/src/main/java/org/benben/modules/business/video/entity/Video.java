package org.benben.modules.business.video.entity;

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
 * @Description: 对视频的管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Data
@TableName("bb_video")
public class Video implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**state*/
	@Excel(name = "state", width = 15)
	private java.lang.Integer state;
	/**视频名称*/
	@Excel(name = "视频名称", width = 15)
	private java.lang.String name;
	/**视频地址*/
	@Excel(name = "视频地址", width = 15)
	private java.lang.String videoUrl;
	/**视频类型*/
	@Excel(name = "视频类型", width = 15)
	private java.lang.String videoType;
	/**视频 1/收费视频 0/ 免费视频*/
	@Excel(name = "视频 1/收费视频 0/ 免费视频", width = 15)
	private java.lang.Integer videoClass;
	/**邀请码*/
	@Excel(name = "邀请码", width = 15)
	private java.lang.String invitecode;
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
	/**修改*/
	@Excel(name = "修改", width = 15)
	private java.lang.String updateBy;
}
