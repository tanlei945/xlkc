package org.benben.modules.business.order.service;

import org.benben.modules.business.order.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.order.vo.OrderBookVo;
import org.benben.modules.business.order.vo.OrderBooksVo;

import java.util.List;

/**
 * @Description: 订单表管理
 * @author： jeecg-boot
 * @date：   2019-05-30
 * @version： V1.0
 */
public interface IOrderService extends IService<Order> {

	OrderBooksVo viewNonPayment(String uid, Integer pageNumber, Integer pageSize);

	OrderBooksVo viewPayment(String id, Integer pageNumber, Integer pageSize);

	OrderBooksVo viewRefund(String id, Integer pageNumber, Integer pageSize);

	List<OrderBookVo> getOrders();

	Order queryOrderNumber(String orderNumber);
}
