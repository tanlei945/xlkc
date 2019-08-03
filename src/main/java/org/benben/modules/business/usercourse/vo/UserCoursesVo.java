package org.benben.modules.business.usercourse.vo;

import lombok.Data;
import org.benben.modules.business.course.vo.CourseVo;
import org.benben.modules.business.usercourse.entity.UserCourse;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class UserCoursesVo {
	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "videoList", width = 15)
	private List<UserCourseVo> userCourseVos;
}
