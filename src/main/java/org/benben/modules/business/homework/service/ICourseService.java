package org.benben.modules.business.homework.service;

import org.benben.modules.business.homework.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 关于课程的表
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
public interface ICourseService extends IService<Course> {

	String queryByCourseIdAndAchieve(String courseId, Integer achieve);
}
