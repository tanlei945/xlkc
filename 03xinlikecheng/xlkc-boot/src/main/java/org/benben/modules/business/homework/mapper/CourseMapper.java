package org.benben.modules.business.homework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.homework.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 关于课程的表
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
public interface CourseMapper extends BaseMapper<Course> {

	@Select("SELECT course_name from bb_course where id=#{courseId} and achieve = #{achieve}")
	String queryByCourseIdAndAchieve(@Param("courseId") String courseId, @Param("achieve") Integer achieve);
}
