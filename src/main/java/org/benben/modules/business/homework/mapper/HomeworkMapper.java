package org.benben.modules.business.homework.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.homework.entity.Homework;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.homework.vo.HomeWorkVo;
import org.benben.modules.business.homework.vo.MyHomeWork;

/**
 * @Description: 管理功课
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface HomeworkMapper extends BaseMapper<Homework> {


	List<HomeWorkVo> queryByUidAndCourseId(String uid);

	@Select("SELECT group_name from bb_course_group where course_id=#{courseId}")
	List<String> queryGroupName(String courseId);

	@Select("select * from bb_course where id = #{courseId}")
	MyHomeWork getCourse(String courseId);
}
