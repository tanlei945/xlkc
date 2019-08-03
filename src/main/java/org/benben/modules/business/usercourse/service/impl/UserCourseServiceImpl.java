package org.benben.modules.business.usercourse.service.impl;

import org.apache.shiro.SecurityUtils;
import org.benben.common.util.DateUtil;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import org.benben.modules.business.courseperinformation.mapper.CoursePerInformationMapper;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.usercourse.entity.UserCourse;
import org.benben.modules.business.usercourse.mapper.UserCourseMapper;
import org.benben.modules.business.usercourse.service.IUserCourseService;
import org.benben.modules.business.usercourse.vo.UserCourseVo;
import org.benben.modules.business.usercourse.vo.UserCoursesVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: 用户课程
 * @author： jeecg-boot
 * @date：   2019-06-01
 * @version： V1.0
 */
@Service
public class UserCourseServiceImpl extends ServiceImpl<UserCourseMapper, UserCourse> implements IUserCourseService {

	@Autowired
	private UserCourseMapper userCourseMapper;

	@Autowired
	private IUserCourseService userCourseService;
	@Autowired
	private CoursePerInformationMapper coursePerInformationMapper;

	@Override
	public UserCoursesVo listPage(String name ,String id, Integer pageNumber, Integer pageSize) {
		//查询天数是否符合
		UserCoursesVo userCoursesVo = new UserCoursesVo();

		//不可以退款
		int day = 30;
		List<UserCourseVo> userCourseVos = userCourseMapper.listPage(day,name, id, pageNumber, pageSize);
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		for (UserCourseVo userCourseVo : userCourseVos) {
			if (userCourseVo.getStatus() == 0) {
				userCourseVo.setStatus(3);
			}
			UserCourse userCourse = new UserCourse();
			BeanUtils.copyProperties(userCourseVo,userCourse);
			userCourseService.updateById(userCourse);

		}
		//可以退款
		List<UserCourseVo> userCourseVos1 = userCourseMapper.listPage1(day,name, id, pageNumber, pageSize);
		for (UserCourseVo userCourseVo : userCourseVos1) {
			if (userCourseVo.getStatus() ==3 ) {
				userCourseVo.setStatus(0);
			}
			UserCourse userCourse = new UserCourse();
			BeanUtils.copyProperties(userCourseVo,userCourse);
			userCourseService.updateById(userCourse);


		}

		List<UserCourseVo> userCourseVoList = userCourseMapper.listPages(name, id, pageNumber, pageSize);
		for (UserCourseVo userCourseVo : userCourseVoList) {
			CoursePerInformation coursePerInformation = coursePerInformationMapper.queryByPerInformation(user.getId(), userCourseVo.getId());
			if (coursePerInformation == null) {
				userCourseVo.setListId("");
			}
			else{
				userCourseVo.setListId(coursePerInformation.getId());
			}
		}
		userCoursesVo.setUserCourseVos(userCourseVoList);
		userCoursesVo.setTotal(userCourseMapper.querAll(name ,id));

		//可以退款的


		//		} else {
		//			System.out.println("不能退款");
		//			List<UserCourseVo> userCourseVos = userCourseMapper.listPage(name ,id, pageNumber, pageSize);
		//			userCoursesVo.setUserCourseVos(userCourseVos);
		//			userCoursesVo.setTotal(userCourseMapper.querAll(name, id));
		//		}
		return userCoursesVo;
	}

	@Override
	public UserCoursesVo queryByName(String id, Integer pageNumber, Integer pageSize, String name) {
		UserCoursesVo userCoursesVo = new UserCoursesVo();
		List<UserCourseVo> userCourseVos = userCourseMapper.queryByName(id, pageNumber, pageSize, name);
		userCoursesVo.setUserCourseVos(userCourseVos);
		userCoursesVo.setTotal(userCourseMapper.querByNameAll(id,name));
		return userCoursesVo;
	}

	@Override
	public List<UserCourseVo> getUserCourseList() {
		return userCourseMapper.getUserCourseList();
	}

	@Override
	public UserCourse queryByCourse(String uid, String courseId) {
		return userCourseMapper.queryByCourse(uid,courseId);
	}
}
