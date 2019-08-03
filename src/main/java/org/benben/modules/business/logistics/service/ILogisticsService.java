package org.benben.modules.business.logistics.service;

import org.benben.modules.business.logistics.entity.Logistics;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 物流信息管理
 * @author： jeecg-boot
 * @date：   2019-06-11
 * @version： V1.0
 */
public interface ILogisticsService extends IService<Logistics> {

	Logistics queryById(String bookId);

	Logistics queryByLogistics(String orderId);

}
