package org.benben.modules.business.homework.service.impl;

import org.benben.modules.business.homework.entity.Course;
import org.benben.modules.business.homework.mapper.CourseMapper;
import org.benben.modules.business.homework.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 关于课程的表
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {

	@Autowired
	private CourseMapper courseMapper;

	@Override
	public String queryByCourseIdAndAchieve(String courseId, Integer achieve) {
		String courseName = courseMapper.queryByCourseIdAndAchieve(courseId,achieve);
		return courseName;
	}
}
