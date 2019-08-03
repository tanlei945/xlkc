package org.benben.modules.business.courseperinformation.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.courseperinformation.entity.CoursePerInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 预约课程个人信息管理
 * @author： jeecg-boot
 * @date：   2019-06-01
 * @version： V1.0
 */
public interface CoursePerInformationMapper extends BaseMapper<CoursePerInformation> {

	@Select("select * from bb_course_per_information where uid = #{uid} and course_id = #{courseId}")
	CoursePerInformation queryByPerInformation(String uid, String courseId);

	@Select("select * from bb_course_per_information where ordernumber = #{orderNumber}")
	CoursePerInformation queryByOrderNumber(String orderNumber);
}
