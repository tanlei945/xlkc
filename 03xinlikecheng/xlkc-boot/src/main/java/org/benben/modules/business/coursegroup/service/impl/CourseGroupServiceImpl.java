package org.benben.modules.business.coursegroup.service.impl;

import org.benben.modules.business.coursegroup.entity.CourseGroup;
import org.benben.modules.business.coursegroup.mapper.CourseGroupMapper;
import org.benben.modules.business.coursegroup.service.ICourseGroupService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 课程小组管理
 * @author： jeecg-boot
 * @date：   2019-07-17
 * @version： V1.0
 */
@Service
public class CourseGroupServiceImpl extends ServiceImpl<CourseGroupMapper, CourseGroup> implements ICourseGroupService {

	@Override
	public List<CourseGroup> queryByCourseId(String id) {
		return null;
	}
}
