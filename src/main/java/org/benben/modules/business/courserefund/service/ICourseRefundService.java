package org.benben.modules.business.courserefund.service;

import org.benben.modules.business.courserefund.entity.CourseRefund;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 课程退款
 * @author： jeecg-boot
 * @date：   2019-06-03
 * @version： V1.0
 */
public interface ICourseRefundService extends IService<CourseRefund> {

	CourseRefund getCourseRefund(String courseId, String id);
}
