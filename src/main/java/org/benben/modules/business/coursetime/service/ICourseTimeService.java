package org.benben.modules.business.coursetime.service;

import org.benben.modules.business.course.entity.Course;
import org.benben.modules.business.coursetime.entity.CourseTime;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;
import java.util.List;

/**
 * @Description: 上课时间管理
 * @author： jeecg-boot
 * @date：   2019-06-21
 * @version： V1.0
 */
public interface ICourseTimeService extends IService<CourseTime> {

	Course getByCourseName(String id);

	String getCourseTimeId(Integer chapter, Date date);

	String getCourseTimeRelevanId(String ctid);

	Course getCourse(String parentId);

	List<Integer> queryById(String id);

	List<CourseTime> getChildTime(String id);
}
