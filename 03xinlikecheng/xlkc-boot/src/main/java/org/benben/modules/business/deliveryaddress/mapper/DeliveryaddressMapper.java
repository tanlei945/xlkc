package org.benben.modules.business.deliveryaddress.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.deliveryaddress.entity.Deliveryaddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 收获地址管理
 * @author： jeecg-boot
 * @date：   2019-05-24
 * @version： V1.0
 */
public interface DeliveryaddressMapper extends BaseMapper<Deliveryaddress> {

	@Select("")
	List<Deliveryaddress> queryBooks(Integer pageNumber, Integer pageSize);

	Integer queryAll();
}
