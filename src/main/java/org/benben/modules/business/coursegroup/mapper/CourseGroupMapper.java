package org.benben.modules.business.coursegroup.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.coursegroup.entity.CourseGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 课程小组管理
 * @author： jeecg-boot
 * @date：   2019-07-17
 * @version： V1.0
 */
public interface CourseGroupMapper extends BaseMapper<CourseGroup> {


	@Select("select * from bb_course_group where course_id = #{id} ")
	List<CourseGroup> queryByCourseId(String id);
}
