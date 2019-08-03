package org.benben.modules.business.courserefund.service.impl;

import org.benben.modules.business.courserefund.entity.CourseRefund;
import org.benben.modules.business.courserefund.mapper.CourseRefundMapper;
import org.benben.modules.business.courserefund.service.ICourseRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 课程退款
 * @author： jeecg-boot
 * @date：   2019-06-03
 * @version： V1.0
 */
@Service
public class CourseRefundServiceImpl extends ServiceImpl<CourseRefundMapper, CourseRefund> implements ICourseRefundService {

	@Autowired
	private CourseRefundMapper courseRefundMapper;

	@Override
	public CourseRefund getCourseRefund(String courseId, String uid) {

		return courseRefundMapper.getCourseRefund(courseId,uid);
	}
}
