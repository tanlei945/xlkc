package org.benben.modules.business.coursetype.service.impl;

import org.benben.modules.business.coursetype.entity.CourseType;
import org.benben.modules.business.coursetype.mapper.CourseTypeMapper;
import org.benben.modules.business.coursetype.service.ICourseTypeService;
import org.benben.modules.business.coursetype.vo.CourseTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 课程分类管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
@Service
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType> implements ICourseTypeService {

	@Autowired
	private CourseTypeMapper courseTypeMapper;

	@Override
	public List<CourseTypeVo> getCourseType(String uid) {
		List<CourseTypeVo> courseTypeVos = courseTypeMapper.getCourseName(uid);
		for (CourseTypeVo courseTypeVo : courseTypeVos) {
			courseTypeVo.setCourseTypeList(courseTypeMapper.getCourseType(courseTypeVo.getCourseId()));
		}
		return courseTypeVos;
	}

	@Override
	public List<CourseType> getCourseTypeName() {
		return courseTypeMapper.getCourseTypeName();
	}
}
