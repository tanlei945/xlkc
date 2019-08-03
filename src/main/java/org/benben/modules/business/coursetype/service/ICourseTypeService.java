package org.benben.modules.business.coursetype.service;

import org.benben.modules.business.coursetype.entity.CourseType;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.coursetype.vo.CourseTypeVo;

import java.util.List;

/**
 * @Description: 课程分类管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
public interface ICourseTypeService extends IService<CourseType> {

	List<CourseTypeVo> getCourseType(String uid);

	List<CourseType> getCourseTypeName();

}
