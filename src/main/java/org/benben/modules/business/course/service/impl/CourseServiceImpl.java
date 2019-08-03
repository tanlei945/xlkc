package org.benben.modules.business.course.service.impl;

import org.apache.shiro.SecurityUtils;
import org.benben.modules.business.books.vo.BookVo;
import org.benben.modules.business.books.vo.BooksVo;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.mapper.CourseMapper;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.course.vo.CourseConfirmVo;
import org.benben.modules.business.course.vo.CourseVo;
import org.benben.modules.business.course.vo.CoursesConfirmVo;
import org.benben.modules.business.course.vo.CoursesVo;
import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import org.benben.modules.business.courseperinformation.mapper.CoursePerInformationMapper;
import org.benben.modules.business.coursetype.entity.CourseType;
import org.benben.modules.business.coursetype.mapper.CourseTypeMapper;
import org.benben.modules.business.homework.vo.HomeworkCourseVo;
import org.benben.modules.business.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 关于课程的管理
 * @author： jeecg-boot
 * @date：   2019-05-31
 * @version： V1.0
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

	@Autowired
	private CourseMapper courseMapper;

	@Autowired
	private CoursePerInformationMapper coursePerInformationMapper;

	@Override
	public CoursesVo queryCourse(Integer pageNumber, Integer pageSize) {
		List<CourseVo> course = courseMapper.queryCourse(pageNumber, pageSize);
		Integer total = courseMapper.queryAll();
		if (total == null){
			total = 0;
		}
		CoursesVo coursesVo = new CoursesVo();
		coursesVo.setVideoList(course);
		coursesVo.setTotal(total);
		return coursesVo;
	}
	@Override
	public HomeworkCourseVo queryByCourseIdAndAchieve(String courseId, String uid) {
		HomeworkCourseVo courseName = courseMapper.queryByCourseIdAndAchieve(courseId,uid);
		return courseName;
	}

	@Override
	public CoursesVo queryByName(String name, Integer pageNumber, Integer pageSize) {
		List<CourseVo> course = courseMapper.queryByName(name,pageNumber, pageSize);
		Integer total = courseMapper.queryByNameAll(name);
		if (total == null){
			total = 0;
		}
		CoursesVo coursesVo = new CoursesVo();
		coursesVo.setVideoList(course);
		coursesVo.setTotal(total);
		return coursesVo;
	}

	@Override
	public CoursesConfirmVo verifyCourse(String uid ,String name, Integer courseVerify,Integer pageNumber, Integer pageSize) {
		List<CourseConfirmVo> list = courseMapper.veriftCourse(uid,name,courseVerify,pageNumber,pageSize);
		Integer total = courseMapper.verifyCourseAll(uid,name, courseVerify);
		if (total == null){
			total = 0;
		}
		CoursesConfirmVo coursesConfirmVo = new CoursesConfirmVo();
		coursesConfirmVo.setCourseConfirmVos(list);
		coursesConfirmVo.setTotal(total);
		return coursesConfirmVo;
	}

	@Override
	public CoursesConfirmVo courseAchieve(String name, String uid,Integer pageNumber, Integer pageSize) {
		//课程已完成模糊查询
		List<CourseConfirmVo> list = courseMapper.courseAchieves(name,uid,pageNumber,pageSize);
		Course course = new Course();
		if (list.size() != 0) {
			for (CourseConfirmVo courseConfirmVo : list) {
				course.setId(courseConfirmVo.getId());
				courseMapper.updateById(course);
			}
		}
		CoursesConfirmVo coursesConfirmVo = new CoursesConfirmVo();

		Integer total = courseMapper.courseAchievesAll(name,uid);
		if (total == null){
			total = 0;
		}
		coursesConfirmVo.setCourseConfirmVos(list);
		coursesConfirmVo.setTotal(total);
		return coursesConfirmVo;
	}

	@Override
	public CoursesConfirmVo courseAssistantAchieve(String name, String uid, Integer pageNumber, Integer pageSize) {
		//课程已完成模糊查询
		List<CourseConfirmVo> list = courseMapper.courseAssistantAchieves(name,uid,pageNumber,pageSize);
		Course course = new Course();
		if (list.size() != 0) {
			for (CourseConfirmVo courseConfirmVo : list) {
				//通过课程id得到该课程的助教日期是否截止
				Course course1 = courseMapper.queryOutTime(courseConfirmVo.getId());
				if (course1 == null) {
					courseConfirmVo.setOutTime(0);
				} else {
					courseConfirmVo.setOutTime(1);
				}
			}
		}
		CoursesConfirmVo coursesConfirmVo = new CoursesConfirmVo();

		Integer total = courseMapper.courseAchievesAll(name,uid);
		if (total == null){
			total = 0;
		}
		coursesConfirmVo.setCourseConfirmVos(list);
		coursesConfirmVo.setTotal(total);
		return coursesConfirmVo;
	}

	@Override
	public CoursesConfirmVo achieveRefund(String name,String uid, Integer pageNumber, Integer pageSize) {
		List<CourseConfirmVo> list = courseMapper.achieveRefund(name, uid,pageNumber,pageSize);
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		for (CourseConfirmVo courseConfirmVo : list) {

			//查询预约个人信息id
			CoursePerInformation coursePerInformation = coursePerInformationMapper.queryByPerInformation(user.getId(), courseConfirmVo.getId());
			if (coursePerInformation == null) {
				courseConfirmVo.setOrderId("");
			}
			else{
				courseConfirmVo.setOrderId(coursePerInformation.getId());
			}
		}
		CoursesConfirmVo coursesConfirmVo = new CoursesConfirmVo();

		Integer total = courseMapper.achieveRefundAll(name,uid);
		if (total == null){
			total = 0;
		}
		coursesConfirmVo.setCourseConfirmVos(list);
		coursesConfirmVo.setTotal(total);
		return coursesConfirmVo;
	}

	@Override
	public List<CourseVo> getCourseName() {
		return courseMapper.getCourseName();
	}

	@Override
	public CoursesVo queryById(String typeId, Integer pageNumber, Integer pageSize) {
		CourseType courseType = new CourseType();
		String courseId = courseMapper.getCourseId(typeId);
		List<CourseVo> course = courseMapper.getCourse(courseId,pageNumber,pageSize);
		Integer total = courseMapper.getCourseAll(courseId);
		if (total == null){
			total = 0;
		}
		CoursesVo coursesVo = new CoursesVo();
		coursesVo.setVideoList(course);
		coursesVo.setTotal(total);
		return coursesVo;
	}

	@Override
	public CoursesVo queryCourses(String typeId, String name, Integer pageNumber, Integer pageSize) {
		String courseId = courseMapper.getCourseId(typeId);
		List<CourseVo> course = courseMapper.queryByNames(courseId,name,pageNumber, pageSize);
		Integer total = courseMapper.queryByNamesAll(courseId,name);
		if (total == null){
			total = 0;
		}
		CoursesVo coursesVo = new CoursesVo();
		coursesVo.setVideoList(course);
		coursesVo.setTotal(total);
		return coursesVo;
	}
}
