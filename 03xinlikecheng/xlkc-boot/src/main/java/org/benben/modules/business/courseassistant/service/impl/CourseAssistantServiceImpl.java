package org.benben.modules.business.courseassistant.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.course.service.ICourseService;
import org.benben.modules.business.courseassistant.entity.CourseAssistant;
import org.benben.modules.business.courseassistant.mapper.CourseAssistantMapper;
import org.benben.modules.business.courseassistant.service.ICourseAssistantService;
import org.benben.modules.business.courseassistant.vo.HomeWorkAchieveVo;
import org.benben.modules.business.coursegroup.entity.CourseGroup;
import org.benben.modules.business.coursegroup.mapper.CourseGroupMapper;
import org.benben.modules.business.coursegroup.service.ICourseGroupService;
import org.benben.modules.business.coursetime.vo.CourseTimeVo;
import org.benben.modules.business.coursetime.vo.Day;
import org.benben.modules.business.coursetime.vo.DayTime;
import org.benben.modules.business.user.entity.User;
import org.benben.modules.business.user.service.IUserService;
import org.benben.modules.business.userleave.mapper.UserLeaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 关于助教的申请
 * @author： jeecg-boot
 * @date：   2019-06-06
 * @version： V1.0
 */
@Service
public class CourseAssistantServiceImpl extends ServiceImpl<CourseAssistantMapper, CourseAssistant> implements ICourseAssistantService {

	@Autowired
	private  CourseAssistantMapper courseAssistantMapper;

	@Autowired
	private UserLeaveMapper userLeaveMapper;

	@Autowired
	private CourseGroupMapper courseGroupMapper;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICourseService courseService;

	@Override
	public CourseTimeVo queryId(String uid, String id) {
		CourseTimeVo courseTimeVo = new CourseTimeVo();
		courseTimeVo = courseAssistantMapper.getCourseName(uid,id);
		List<DayTime> date = courseAssistantMapper.getDate(uid, id);
		for (DayTime dayTime : date) {
			List<Day> day = userLeaveMapper.getDay(dayTime.getId());
			dayTime.setDays(day);
		}
		courseTimeVo.setDates(date);
		return courseTimeVo;
	}

	@Override
	public List<HomeWorkAchieveVo> homeworkAchieve(String uid) {
		List<HomeWorkAchieveVo> homeWorkAchieveVo = courseAssistantMapper.homeworkAchieve(uid);
		boolean type = false;
		for (HomeWorkAchieveVo workAchieveVo : homeWorkAchieveVo) {
			//通过课程id得到课程小组名称和小组成员
			List<CourseGroup> courseGroups = courseGroupMapper.queryByCourseId(workAchieveVo.getId());
			CourseGroup courseGroup = new CourseGroup();
			for (CourseGroup cg : courseGroups) {
				String crewName = (String) cg.getCrewName();
				String[] c = crewName.split(",");
				for (String s : c) {
					if (s.equals(uid)) {
						courseGroup = cg;
						type = true;
						break;
					}
				}
				if (type == true) {
					break;
				}
			}
			if (courseGroup == null) {
				continue;
			}

			workAchieveVo.setGroupName(courseGroup.getGroupName());
			//把小组成员的id转换为用户昵称
			String crewNames = (String) courseGroup.getCrewName();
			if(StringUtils.isNotBlank(crewNames)){
				String[] crewName = crewNames.split(",");
				String[] nickname = new String[crewName.length];
				for (int i = 0; i < crewName.length; i++) {
					User user = userService.getById(crewName[i]);
					nickname[i] = user.getNickname();
				}
				String nicknames = StringUtils.join(nickname, ",");
				workAchieveVo.setCrewName(nicknames);
			}
		}
		return homeWorkAchieveVo;
	}

	@Override
	public HomeWorkAchieveVo homeworkById(String courseId) {
		User user1 = (User) SecurityUtils.getSubject().getPrincipal();

		boolean type = false;

		HomeWorkAchieveVo homeWorkAchieveVo = new HomeWorkAchieveVo();
		//得到课程的信息
		Course course = courseService.getById(courseId);
		homeWorkAchieveVo.setId(course.getId());
		homeWorkAchieveVo.setLanguage(course.getLanguage());
		homeWorkAchieveVo.setCourseName(course.getCourseName());
		//通过课程id得到课程小组名称和小组成员
		List<CourseGroup> courseGroups = courseGroupMapper.queryByCourseId(homeWorkAchieveVo.getId());

		CourseGroup courseGroup = new CourseGroup();
		for (CourseGroup cg : courseGroups) {
			String crewName = (String) cg.getCrewName();
			String[] c = crewName.split(",");
			for (String s : c) {
				if (s.equals(user1.getId())) {
					courseGroup = cg;
					type = true;
					break;
				}
			}
			if (type == true) {
				break;
			}
		}
		if (courseGroup == null) {
			return null;
		}

		homeWorkAchieveVo.setGroupName(courseGroup.getGroupName());
		//把小组成员的id转换为用户昵称
		String crewNames = (String) courseGroup.getCrewName();
		String[] crewName = crewNames.split(",");
		String[] nickname = new String[crewName.length];
		for (int i = 0; i < crewName.length; i++) {
			User user = userService.getById(crewName[i]);
			nickname[i] = user.getNickname();
		}
		String nicknames = StringUtils.join(nickname, ",");
		homeWorkAchieveVo.setCrewName(nicknames);
		return homeWorkAchieveVo;
	}
}
