package org.benben.modules.business.course.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class CoursesConfirmVo {
	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "courseConfirmList", width = 15)
	private List<CourseConfirmVo> courseConfirmVos;
}
