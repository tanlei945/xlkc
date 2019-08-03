package org.benben.modules.business.usercourse.service;

import org.benben.modules.business.usercourse.entity.UserCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.usercourse.vo.UserCourseVo;
import org.benben.modules.business.usercourse.vo.UserCoursesVo;

import java.util.List;

/**
 * @Description: 用户课程
 * @author： jeecg-boot
 * @date：   2019-06-01
 * @version： V1.0
 */
public interface IUserCourseService extends IService<UserCourse> {

	UserCoursesVo listPage(String name ,String id, Integer pageNumber, Integer pageSize);

	UserCoursesVo queryByName(String id, Integer pageNumber, Integer pageSize,String name);

	List<UserCourseVo> getUserCourseList();

	UserCourse queryByCourse(String uid, String courseId);

}
