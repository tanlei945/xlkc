package org.benben.modules.business.userresponse.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.userresponse.entity.UserResponse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.userresponse.vo.UserResponseVo;

/**
 * @Description: 用户回复表管理
 * @author： jeecg-boot
 * @date：   2019-06-11
 * @version： V1.0
 */
public interface UserResponseMapper extends BaseMapper<UserResponse> {

	@Select("select * from user_response where evaluate_id=#{id}")
	List<UserResponseVo> queryByEvaluateId(String id);
	@Select("select * from user_response where evaluate_id=#{id}")
	List<UserResponse> queryByEvaluateId1(String id);

	@Select("select nickname from user where id = #{id}")
	String queryById(String id);
}
