package org.benben.modules.business.course.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.course.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.course.vo.CourseConfirmVo;
import org.benben.modules.business.course.vo.CourseVo;
import org.benben.modules.business.course.vo.CoursesVo;
import org.benben.modules.business.homework.vo.HomeworkCourseVo;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description: 关于课程的管理
 * @author： jeecg-boot
 * @date：   2019-05-31
 * @version： V1.0
 */
public interface CourseMapper extends BaseMapper<Course> {

	@Select("select * from bb_course limit #{pageNumber},#{pageSize}")
	List<CourseVo> queryCourse(@Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("select count(*) from bb_course")
	Integer queryAll();

	@Select("SELECT b.* from user_course c INNER JOIN bb_course b on c.course_id = b.id where c.course_id=#{courseId} and  b.endtime < NOW() and c.uid = #{uid}")
	HomeworkCourseVo queryByCourseIdAndAchieve(@Param("courseId") String courseId, @Param("uid") String uid);

	List<CourseVo> queryByName(@Param("name") String name,@Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	Integer queryByNameAll(@Param("name") String name);

	//课程确认
	//@Select("select * from bb_course where course_verify = #{courseVerify} limit #{pageNumber},#{pageSize}")
	@Select("select c.* from  user_course uc INNER JOIN bb_course c on c.id= uc.course_id where  uc.course_verify = #{courseVerify} and uc.uid=#{uid} and c.course_name like '%${name}%' limit #{pageNumber},#{pageSize}")
	List<CourseConfirmVo> veriftCourse(@Param("uid") String uid,@Param("name") String name, @Param("courseVerify") Integer courseVerify, @Param("pageNumber") Integer pageNumber,@Param("pageSize") Integer pageSize);

//	@Select("select count(1) from bb_course where course_verify = #{courseVerify}")
	@Select("select count(*) from  user_course uc INNER JOIN bb_course c on c.id= uc.course_id where uc.course_verify = #{courseVerify} and uc.uid=#{uid} and c.course_name like '%${name}%'")
	Integer verifyCourseAll(@Param("uid")String uid, @Param("name") String name, @Param("courseVerify") Integer courseVerify);

	//课程已完成
//	@Select("select * from bb_course where endtime < NOW() limit #{pageNumber},#{pageSize}")
	@Select("select c.* from  user_course uc INNER JOIN bb_course c on c.id= uc.course_id  where  c.endtime < NOW() and uc.uid=#{uid}  limit #{pageNumber},#{pageSize}")
	List<CourseConfirmVo> courseAchieve( @Param("uid") String uid, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("select count(*) from  user_course uc INNER JOIN bb_course c on c.id= uc.course_id where  c.endtime < NOW() and uc.uid=#{uid}")
	Integer courseAchieveAll( @Param("uid") String uid);
	//课程已完成退款
	@Select("select c.* from  user_course uc INNER JOIN bb_course c on c.id= uc.course_id  where  c.endtime < NOW() and uc.uid=#{uid} and uc.status = '2' and c.course_name like '%${name}%' limit #{pageNumber},#{pageSize}")
	List<CourseConfirmVo> achieveRefund(@Param("name") String name , @Param("uid") String uid, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("select count(*) from  user_course uc INNER JOIN bb_course c on c.id= uc.course_id where  c.endtime < NOW() and uc.uid=#{uid} and uc.status = '2' and c.course_name like '%${name}%'")
	Integer achieveRefundAll(String name,String uid);

	@Select("select id,course_name from bb_course")
	List<CourseVo> getCourseName();

	@Select("select course_id from bb_course_type where id = #{typeId} ")
	String getCourseId(String typeId);

	@Select("select * from bb_course where id = #{courseId} limit #{pageNumber},#{pageSize}")
	List<CourseVo>getCourse(@Param("courseId")String courseId, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("select count(*) from bb_course where id = #{courseId}")
	Integer getCourseAll(String courseId);

	@Select("select * from bb_course where id = #{courseId} and course_name like '%${name}%' limit #{pageNumber},#{pageSize}")
	List<CourseVo> queryByNames(@Param("courseId") String courseId, @Param("name") String name, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("select count(*) from bb_course where id = #{courseId} and course_name like '%${name}%' ")
	Integer queryByNamesAll(@Param("courseId") String courseId, @Param("name") String name);

	//课程已完成模糊查询
	@Select("select c.* from  user_course uc INNER JOIN bb_course c on c.id= uc.course_id  where  c.endtime < NOW() and uc.uid=#{uid} and c.course_name like '%${name}%'  limit #{pageNumber},#{pageSize}")
	List<CourseConfirmVo> courseAchieves(@Param("name")String name, @Param("uid") String uid, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("select c.* from  user_course uc INNER JOIN bb_course c on c.id= uc.course_id  where  c.endtime < NOW() and uc.uid=#{uid} and c.course_name like '%${name}%'  limit #{pageNumber},#{pageSize}")
	List<CourseConfirmVo> courseAssistantAchieves(@Param("name")String name, @Param("uid") String uid, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("select count(*) from  user_course uc INNER JOIN bb_course c on c.id= uc.course_id where  c.endtime < NOW() and uc.uid=#{uid} and c.course_name like '%${name}%' ")
	Integer courseAchievesAll(@Param("name")String name, @Param("uid") String uid);

	@Select("select count(*) from  user_course uc INNER JOIN bb_course c on c.id= uc.course_id where  c.endtime < NOW() and uc.uid=#{uid} and c.course_name like '%${name}%' ")
	Integer courseAssistantAchievesAll(@Param("name")String name, @Param("uid") String uid);

	@Select("SELECT * from bb_course where expiration_time < NOW() and id = #{id}")
	Course queryOutTime(String id);
}
