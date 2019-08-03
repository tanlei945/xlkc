package org.benben.modules.business.coursetype.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.vo.CourseVo;
import org.benben.modules.business.coursetype.entity.CourseType;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class CourseTypeVo {
	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String courseId;
	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
	private java.lang.String courseName;

	@Excel(name = "courseTypeList", width = 15)
	private List<CourseType> courseTypeList;

}
