package org.benben.modules.business.coursetype.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.coursetype.entity.CourseType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.coursetype.vo.CourseTypeVo;

/**
 * @Description: 课程分类管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
public interface CourseTypeMapper extends BaseMapper<CourseType> {

	@Select("select uc.course_id,c.course_name from user_course uc inner join bb_course c on c.id = uc.course_id  where uid= #{uid} ")
	List<CourseTypeVo> getCourseName(String uid);

	@Select("select * from bb_course_type where course_id = #{courseId}")
	List<CourseType> getCourseType(String courseId);

	@Select("select * from bb_course_type")
	List<CourseType> getCourseTypeName();
}
