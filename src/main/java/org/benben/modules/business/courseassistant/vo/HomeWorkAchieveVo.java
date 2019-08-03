package org.benben.modules.business.courseassistant.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class HomeWorkAchieveVo {
	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
	private java.lang.String courseName;
	/**语种 0/普通话 1/粤语*/
	@Excel(name = "语种 0/普通话 1/粤语", width = 15)
	private java.lang.Integer language;
	/**小组名称*/
	@Excel(name = "小组名称", width = 15)
	private java.lang.Object groupName;
	/**组员名称*/
	@Excel(name = "组员名称", width = 15)
	private java.lang.Object crewName;
}
