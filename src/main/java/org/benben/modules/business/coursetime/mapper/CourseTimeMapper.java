package org.benben.modules.business.coursetime.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.coursetime.entity.CourseTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 上课时间管理
 * @author： jeecg-boot
 * @date：   2019-06-21
 * @version： V1.0
 */
public interface CourseTimeMapper extends BaseMapper<CourseTime> {

	@Select("SELECT c.* from bb_course_time ct INNER JOIN bb_course_time_relevan ctr on ct.id = ctr.ctid INNER JOIN bb_course c on c.id = ctr.cid where ct.id= #{id}")
	Course getByCourseName(String id);

	@Select("select id from bb_course_time where chapter = #{chapter} and date = #{date}")
	String getCourseTimeId(@Param("chapter") Integer chapter, @Param("date") Date date);

	@Select("select id from bb_course_time_relevan where ctid = #{ctid}")
	String getCourseTimeRelevanId(String ctid);

	@Select("SELECT * from bb_course WHERE id = (SELECT cid from bb_course_time_relevan where ctid =#{parentId})")
	Course getCourse1(String parentId);
	@Select("SELECT * from bb_course WHERE id = (SELECT cid from bb_course_time_relevan where ctid =#{parentId})")
	Course getCourse(String parentId);

	@Select("select day from bb_course_time where parent_id = #{id}")
	List<Integer> queryById(String id);

	@Select("select * from bb_course_time where parent_id = #{id}")
	List<CourseTime> getChildTime(String id);
}