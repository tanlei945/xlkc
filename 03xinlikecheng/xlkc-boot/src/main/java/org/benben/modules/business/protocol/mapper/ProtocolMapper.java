package org.benben.modules.business.protocol.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.protocol.entity.Protocol;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 协议管理
 * @author： jeecg-boot
 * @date：   2019-05-25
 * @version： V1.0
 */
public interface ProtocolMapper extends BaseMapper<Protocol> {

	@Select("select * from bb_protocol where type = #{type}")
	Protocol queryByType(Integer type);
}
