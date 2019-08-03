package org.benben.modules.business.courseassistanttime.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class TimeVo {
	@Excel(name = "课程月份", width = 15, format = "yyyy-MM")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM")
	@DateTimeFormat(pattern="yyyy-MM")
	private java.util.Date date;
	@Excel(name = "对应的日期天数", width = 15)
	private List<Integer> days;
}
