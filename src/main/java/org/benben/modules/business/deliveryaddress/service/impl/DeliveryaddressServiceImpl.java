package org.benben.modules.business.deliveryaddress.service.impl;

import org.benben.modules.business.books.vo.BookVo;
import org.benben.modules.business.books.vo.BooksVo;
import org.benben.modules.business.deliveryaddress.entity.Deliveryaddress;
import org.benben.modules.business.deliveryaddress.mapper.DeliveryaddressMapper;
import org.benben.modules.business.deliveryaddress.service.IDeliveryaddressService;
import org.benben.modules.business.deliveryaddress.vo.DeliveryaddresVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 收获地址管理
 * @author： jeecg-boot
 * @date：   2019-05-24
 * @version： V1.0
 */
@Service
public class DeliveryaddressServiceImpl extends ServiceImpl<DeliveryaddressMapper, Deliveryaddress> implements IDeliveryaddressService {

	@Autowired
	private DeliveryaddressMapper deliveryaddressMapper;

	@Override
	public DeliveryaddresVo listPage(Integer pageNumber, Integer pageSize) {
		List<Deliveryaddress> deliveryaddresVos = deliveryaddressMapper.queryBooks(pageNumber, pageSize);
		Integer total = deliveryaddressMapper.queryAll();
		if (total == null){
			total = 0;
		}
		DeliveryaddresVo deliveryaddresVos1 = new DeliveryaddresVo();
		deliveryaddresVos1.setDeliveryaddress(deliveryaddresVos);
		deliveryaddresVos1.setTotal(total);
		return deliveryaddresVos1;
	}
}
