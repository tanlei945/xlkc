package org.benben.modules.business.userbuke.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.userbuke.entity.UserBuke;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.userbuke.vo.UserBukeVo;
import org.benben.modules.business.userleave.vo.UserCourseStateVo;

/**
 * @Description: 用户补课的管理
 * @author： jeecg-boot
 * @date：   2019-06-05
 * @version： V1.0
 */
public interface UserBukeMapper extends BaseMapper<UserBuke> {


	@Select("SELECT c.*,b.status from user_buke b INNER JOIN bb_course c on b.course_id= c.id where b.uid = #{uid} limit #{pageNumber},#{pageSize}")
	List<UserBukeVo> listPage(@Param("uid") String uid, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("SELECT count(1) from user_buke b INNER JOIN bb_course c on b.course_id= c.id where b.uid = #{uid}")
	Integer queryAll(String uid);

	@Select("select c.* from user_course uc INNER JOIN bb_course c on uc.course_id = c.id where uc.uid =#{uid} and c.course_name like '%${name}%'  limit #{pageNumber},#{pageSize}")
	List<UserBukeVo> queryByName(@Param("uid") String uid, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize, @Param("name") String name);

/*	@Select("select count(1) from user_course uc INNER JOIN bb_course c on uc.course_id = c.id where uc.uid =#{uid}")
	Integer querByNameAll(@Param("uid") String id, @Param("name") String name);*/

	@Select("select count(1) from user_course uc INNER JOIN bb_course c on uc.course_id = c.id where uc.uid =#{uid}")
	Integer queryByNameAll(@Param("uid") String id, @Param("name") String name);

	@Select("select * from user_buke where uid = #{uid}")
	List<UserBuke> queryByUid(String uid);
}
