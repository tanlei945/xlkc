package org.benben.modules.business.courseperinformation.service.impl;

import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import org.benben.modules.business.courseperinformation.mapper.CoursePerInformationMapper;
import org.benben.modules.business.courseperinformation.service.ICoursePerInformationService;
import org.benben.modules.business.usercourse.entity.UserCourse;
import org.benben.modules.business.usercourse.mapper.UserCourseMapper;
import org.benben.modules.business.usercourse.service.IUserCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 预约课程个人信息管理
 * @author： jeecg-boot
 * @date：   2019-06-01
 * @version： V1.0
 */
@Service
public class CoursePerInformationServiceImpl extends ServiceImpl<CoursePerInformationMapper, CoursePerInformation> implements ICoursePerInformationService {

	@Autowired
	private CoursePerInformationMapper coursePerInformationMapper;

	@Autowired
	private UserCourseMapper userCourseMapper;

	@Override
	public CoursePerInformation queryByPerInformation(String uid, String courseId) {
		CoursePerInformation coursePerInformation = coursePerInformationMapper.queryByPerInformation(uid, courseId);
		UserCourse userCourse = userCourseMapper.queryByCourse(uid, courseId);
		if (userCourse == null) {
			return null;
		} else {
			return coursePerInformation;
		}
	}

	@Override
	public CoursePerInformation queryByOrdeNumber(String orderNumber) {
		return coursePerInformationMapper.queryByOrderNumber(orderNumber);
	}
}
