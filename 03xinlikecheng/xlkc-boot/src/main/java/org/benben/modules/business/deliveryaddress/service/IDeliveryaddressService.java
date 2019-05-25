package org.benben.modules.business.deliveryaddress.service;

import org.benben.modules.business.deliveryaddress.entity.Deliveryaddress;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.deliveryaddress.vo.DeliveryaddresVo;

/**
 * @Description: 收获地址管理
 * @author： jeecg-boot
 * @date：   2019-05-24
 * @version： V1.0
 */
public interface IDeliveryaddressService extends IService<Deliveryaddress> {

	DeliveryaddresVo listPage(Integer pageNumber, Integer pageSize);

}
