package org.benben.modules.business.usercourse.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.usercourse.entity.UserCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.usercourse.vo.UserCourseVo;

import javax.xml.crypto.Data;

/**
 * @Description: 用户课程
 * @author： jeecg-boot
 * @date：   2019-06-01
 * @version： V1.0
 */
public interface UserCourseMapper extends BaseMapper<UserCourse> {

	@Select("SELECT DATEDIFF(c.starttime,now()) from bb_course c INNER JOIN user_course uc on uc.course_id = c.id where uc.uid= #{uid}  ")
	Integer queryDay(String uid);

	@Select("SELECT c.*,uc.status from bb_course c INNER JOIN user_course uc on uc.course_id = c.id where uc.uid= #{uid} and (SELECT DATEDIFF(c.starttime,now())) < #{day} and c.course_name like '%${name}%'  limit #{pageNumber},#{pageSize} ")
	List<UserCourseVo> listPage(@Param("day") Integer day,@Param("name")String name, @Param("uid") String uid, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("SELECT count(*) from bb_course c INNER JOIN user_course uc on uc.course_id = c.id where uc.uid= #{uid} and c.course_name like '%${name}%'")
	Integer querByNameAll(String uid, String name);

	@Select("SELECT count(*) from bb_course c INNER JOIN user_course uc on uc.course_id = c.id where uc.uid= #{uid} and (SELECT DATEDIFF(c.starttime,now())) < 30 and c.course_name like '%${name}%' ")
	Integer querAll(@Param("uid") String uid,@Param("name")String name);

	@Select("SELECT c.*,uc.status from bb_course c INNER JOIN user_course uc on uc.course_id = c.id where uc.uid= #{uid} and c.course_name like '%${name}%' limit #{pageNumber},#{pageSize} ")
	List<UserCourseVo> queryByName(@Param("uid") String id, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize, @Param("name") String name);

	@Select("select * from user_course uc INNER JOIN bb_course c on uc.course_id= c.id")
	List<UserCourseVo> getUserCourseList();

	@Select("SELECT c.*,uc.status from bb_course c INNER JOIN user_course uc on uc.course_id = c.id where uc.uid= #{uid} and (SELECT DATEDIFF(c.starttime,now())) > #{day} and c.course_name like '%${name}%'  limit #{pageNumber},#{pageSize} ")
	List<UserCourseVo> listPage1(@Param("day") Integer day,@Param("name")String name, @Param("uid") String uid, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("SELECT c.*,uc.status from bb_course c INNER JOIN user_course uc on uc.course_id = c.id where uc.uid= #{uid} and  c.course_name like '%${name}%'  limit #{pageNumber},#{pageSize} ")
	List<UserCourseVo> listPages(@Param("name")String name, @Param("uid") String uid, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("select * from user_course where course_id = #{courseId} and uid =#{uid}")
	UserCourse queryByCourse(String uid, String courseId);

}
