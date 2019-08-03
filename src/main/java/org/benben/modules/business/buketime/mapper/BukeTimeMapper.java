package org.benben.modules.business.buketime.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.buketime.entity.BukeTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.coursetime.vo.Day;
import org.benben.modules.business.coursetime.vo.DayTime;

/**
 * @Description: 补课时间管理
 * @author： jeecg-boot
 * @date：   2019-07-18
 * @version： V1.0
 */
public interface BukeTimeMapper extends BaseMapper<BukeTime> {

	@Select("select * from bb_buke_time where uid = #{id} and user_buke_id = #{bukeId}")
	List<DayTime> getTime(String id, String bukeId);

	@Select("select * from bb_buke_time where parent_id = #{id}")
	List<Day> getDay(String id);

	@Select("select * from bb_buke_time where parent_id = #{parentId}")
	List<BukeTime> getByParentId(String parentId);
}
