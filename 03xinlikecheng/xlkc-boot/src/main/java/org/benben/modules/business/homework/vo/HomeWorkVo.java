package org.benben.modules.business.homework.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class HomeWorkVo {
	/**课程相关联id*/
	@Excel(name = "g功课", width = 15)
	private java.lang.String id;
	@Excel(name = "课程相关联id", width = 15)
	private java.lang.String courseId;
	/**小组名称*/
	@Excel(name = "小组名称", width = 15)
	private java.lang.String groupName;
	/**组员*/
	@Excel(name = "组员", width = 15)
	private java.lang.String crew;
	@Excel(name = "课程年份", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date courseTime;

	@Excel(name = "语种", width = 15)
	private java.lang.Integer language;
	/**文档路径*/
	@Excel(name = "文档路径", width = 15)
	private java.lang.String wordUrl;
	/**视频路径*/
	@Excel(name = "视频路径", width = 15)
	private java.lang.String videoUrl;

}
