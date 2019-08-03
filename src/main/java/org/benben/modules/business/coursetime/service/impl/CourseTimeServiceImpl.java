package org.benben.modules.business.coursetime.service.impl;

import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.coursetime.entity.CourseTime;
import org.benben.modules.business.coursetime.mapper.CourseTimeMapper;
import org.benben.modules.business.coursetime.service.ICourseTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;

/**
 * @Description: 上课时间管理
 * @author： jeecg-boot
 * @date：   2019-06-21
 * @version： V1.0
 */
@Service
public class CourseTimeServiceImpl extends ServiceImpl<CourseTimeMapper, CourseTime> implements ICourseTimeService {

	@Autowired
	private CourseTimeMapper courseTimeMapper;

	@Override
	public Course getByCourseName(String id) {
		return courseTimeMapper.getByCourseName(id);
	}

	@Override
	public String getCourseTimeId(Integer chapter, Date date) {
		return courseTimeMapper.getCourseTimeId(chapter,date);
	}

	@Override
	public String getCourseTimeRelevanId(String ctid) {
		return courseTimeMapper.getCourseTimeRelevanId(ctid);
	}

	@Override
	public Course getCourse(String parentId) {
		return courseTimeMapper.getCourse(parentId);
	}

	@Override
	public List<Integer> queryById(String id) {
		return courseTimeMapper.queryById(id);
	}

	@Override
	public List<CourseTime> getChildTime(String id) {
		return courseTimeMapper.getChildTime(id);
	}
}
