package org.benben.modules.business.courseassistant.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.courseassistant.entity.CourseAssistant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.courseassistant.vo.HomeWorkAchieveVo;
import org.benben.modules.business.coursetime.vo.CourseTimeVo;
import org.benben.modules.business.coursetime.vo.DayTime;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description: 关于助教的申请
 * @author： jeecg-boot
 * @date：   2019-06-06
 * @version： V1.0
 */
public interface CourseAssistantMapper extends BaseMapper<CourseAssistant> {

	@Select("SELECT c.* from user_course uc INNER JOIN bb_course c on uc.course_id  = c.id where uc.uid = #{uid} and c.id= #{id}")
	CourseTimeVo getCourseName(@Param("uid") String uid, @Param("id") String id);

	@Select("SELECT ct.* from user_course uc INNER JOIN bb_course c on uc.course_id = c.id INNER JOIN bb_course_time_relevan ctr on ctr.cid = c.id INNER JOIN bb_course_time ct on ct.id = ctr.ctid where uc.uid = #{uid} and  c.id = #{id}")
	List<DayTime> getDate(@Param("uid") String uid, @Param("id") String id);

	@Select("select c.* from  user_course uc INNER JOIN bb_course c on c.id= uc.course_id  where  c.endtime < NOW() and uc.uid=#{uid}")
	List<HomeWorkAchieveVo> homeworkAchieve(String uid);
}
