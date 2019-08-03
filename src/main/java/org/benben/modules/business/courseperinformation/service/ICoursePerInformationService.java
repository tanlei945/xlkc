package org.benben.modules.business.courseperinformation.service;

import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 预约课程个人信息管理
 * @author： jeecg-boot
 * @date：   2019-06-01
 * @version： V1.0
 */
public interface ICoursePerInformationService extends IService<CoursePerInformation> {

	CoursePerInformation queryByPerInformation(String id, String courseId);

	CoursePerInformation queryByOrdeNumber(String orderNumber);
}
