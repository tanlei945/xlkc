package org.benben.modules.business.logistics.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.logistics.entity.Logistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 物流信息管理
 * @author： jeecg-boot
 * @date：   2019-06-11
 * @version： V1.0
 */
public interface LogisticsMapper extends BaseMapper<Logistics> {

	@Select("select * from bb_logistics where order_id = #{ordernumber}")
	Logistics queryById(String ordernumber);

}
