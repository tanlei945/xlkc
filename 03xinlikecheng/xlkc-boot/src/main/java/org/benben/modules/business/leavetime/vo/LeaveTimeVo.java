package org.benben.modules.business.leavetime.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class LeaveTimeVo {
	/**用户id*/
	@Excel(name = "用户名称", width = 15)
	private java.lang.String nickName;
	/**用户请假月*/
	@Excel(name = "用户请假月", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date date;
	/**parentId*/
	@Excel(name = "parentId", width = 15)
	private java.lang.String parentId;
	/**对应的日期天数*/
	@Excel(name = "对应的日期天数", width = 15)
	private java.lang.Integer day;
}
