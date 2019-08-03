package org.benben.modules.business.course.service;

import org.benben.modules.business.course.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.course.vo.CourseConfirmVo;
import org.benben.modules.business.course.vo.CourseVo;
import org.benben.modules.business.course.vo.CoursesConfirmVo;
import org.benben.modules.business.course.vo.CoursesVo;
import org.benben.modules.business.homework.vo.HomeworkCourseVo;
import org.benben.modules.business.usercourse.vo.UserCoursesVo;

import java.util.List;

/**
 * @Description: 关于课程的管理
 * @author： jeecg-boot
 * @date：   2019-05-31
 * @version： V1.0
 */
public interface ICourseService extends IService<Course> {

	CoursesVo queryCourse(Integer pageNumber, Integer pageSize);

	HomeworkCourseVo queryByCourseIdAndAchieve(String courseId,String uid);

	CoursesVo queryByName(String name, Integer pageNumber, Integer pageSize);

	CoursesConfirmVo verifyCourse(String uid,String name, Integer courseVerify,Integer pageNumber, Integer pageSize);

	CoursesConfirmVo courseAchieve(String name, String uid,Integer apgeNumber, Integer pageSize);
	CoursesConfirmVo courseAssistantAchieve(String name, String uid,Integer apgeNumber, Integer pageSize);

	CoursesConfirmVo achieveRefund(String name, String id, Integer pageNumber, Integer pageSize);

	List<CourseVo> getCourseName();

	CoursesVo queryById(String typeId, Integer pageNumber, Integer pageSize);

	CoursesVo queryCourses(String typeId, String name, Integer pageNumber, Integer pageSize);
}
