package org.benben.modules.business.course.vo;

import lombok.Data;
import org.benben.modules.business.books.vo.BookVo;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class CoursesVo {

	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "videoList", width = 15)
	private List<CourseVo> videoList;
}
