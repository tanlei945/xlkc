package org.benben.modules.business.coursegroup.service;

import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.coursegroup.entity.CourseGroup;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 课程小组管理
 * @author： jeecg-boot
 * @date：   2019-07-17
 * @version： V1.0
 */
public interface ICourseGroupService extends IService<CourseGroup> {

	List<CourseGroup> queryByCourseId(String id);
}
