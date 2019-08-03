package org.benben.modules.business.courserefund.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.courserefund.entity.CourseRefund;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 课程退款
 * @author： jeecg-boot
 * @date：   2019-06-03
 * @version： V1.0
 */
public interface CourseRefundMapper extends BaseMapper<CourseRefund> {

	@Select("select * from bb_course_refund where course_id= #{courseId} and uid =#{uid}")
	CourseRefund getCourseRefund(String courseId, String uid);
}
