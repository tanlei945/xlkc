package org.benben.modules.business.userleave.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.coursetime.entity.CourseTime;
import org.benben.modules.business.coursetime.vo.Day;
import org.benben.modules.business.coursetime.vo.DayTime;
import org.benben.modules.business.userleave.entity.UserLeave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.userleave.vo.UserCourseStateVo;
import org.benben.modules.business.userleave.vo.UserLeaveVo;

/**
 * @Description: 用户请假
 * @author： jeecg-boot
 * @date：   2019-06-03
 * @version： V1.0
 */
public interface UserLeaveMapper extends BaseMapper<UserLeave> {
	@Select("SELECT DATEDIFF(ul.start_time,now())from bb_course c INNER JOIN user_leave ul on ul.course_id = c.id where ul.uid= #{uid}")
	Integer queryDay(String uid);

	/*@Select("SELECT c.* from bb_course c INNER JOIN user_course uc on uc.course_id = c.id INNER JOIN user_leave ul on ul.uc_id = uc.id where uc.uid=#{uid} limit #{pageNumber},#{pageSize} ")
	List<UserLeaveVo> listPage(@Param("uid") String uid, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);
*/
	@Select("select count(1) from user_course uc INNER JOIN bb_course c on uc.course_id = c.id where uc.uid =#{uid}")
	Integer querAll(String uid);

	//ct.start_time,ct.end_time,c.course_name,c.starttime,c.endtime
	@Select("select * from bb_leave_time where user_leave_id = #{leaveId} and uid = #{id}")
	List<DayTime> getTime(String id, String leaveId);

	@Select("select * from user_leave where uc_id = #{ucid}")
	UserLeave queryByCid(String ucid);

	@Select("select c.* from user_course uc INNER JOIN bb_course c on uc.course_id = c.id where uc.uid =#{uid} limit #{pageNumber},#{pageSize}")
	List<UserCourseStateVo> queryCourse(String uid,  @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("select ul.status from  bb_course_time_relevan ctr INNER JOIN bb_course_time ct on ct.id = ctr.ctid INNER JOIN user_leave ul on ul.course_id = ct.id where ul.user_id=#{uid} and ctr.cid =#{cid}")
	List<Integer> queryState(@Param("uid") String uid, @Param("cid") String cid);

	@Select("select c.* from user_course uc INNER JOIN bb_course c on uc.course_id = c.id where uc.uid =#{uid} and c.course_name like '%${name}%'  limit #{pageNumber},#{pageSize}")
	List<UserCourseStateVo> queryByName(@Param("uid") String uid, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize, @Param("name") String name);

	@Select("select count(1) from user_course uc INNER JOIN bb_course c on uc.course_id = c.id where uc.uid =#{uid} and c.course_name like '%${name}%'")
	Integer querByNameAll(@Param("uid") String uid, @Param("name") String name);

	@Select("select day from bb_leave_time where parent_id = #{id}")
	List<Day> getDay(String id);

	@Select("select * from user_leave where user_id = #{uid}")
	List<UserLeave> queryByUid(String uid);
}
