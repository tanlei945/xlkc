package org.benben.modules.business.order.service.impl;

import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.mapper.OrderMapper;
import org.benben.modules.business.order.service.IOrderService;
import org.benben.modules.business.order.vo.OrderBookVo;
import org.benben.modules.business.order.vo.OrderBooksVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 订单表管理
 * @author： jeecg-boot
 * @date：   2019-05-30
 * @version： V1.0
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

	@Autowired
	private OrderMapper orderMapper;

	@Override
	public OrderBooksVo viewNonPayment(String uid, Integer pageNumber, Integer pageSize) {
		List<OrderBookVo> orderBookVos = orderMapper.viewNonPayment(uid, pageNumber, pageSize);
		Integer total = orderMapper.viewNonPaymentAll(uid);
		if (total == null) {
			total = 0;
		}
		OrderBooksVo orderBooksVo = new OrderBooksVo();
		orderBooksVo.setTotal(total);
		orderBooksVo.setOrderBooksVos(orderBookVos);
		return orderBooksVo;
	}

	@Override
	public OrderBooksVo viewPayment(String uid, Integer pageNumber, Integer pageSize) {
		List<OrderBookVo> orderBookVos = orderMapper.viewPayment(uid, pageNumber, pageSize);
		Integer total = orderMapper.viewPaymentAll(uid);
		if (total == null) {
			total = 0;
		}
		OrderBooksVo orderBooksVo = new OrderBooksVo();
		orderBooksVo.setTotal(total);
		orderBooksVo.setOrderBooksVos(orderBookVos);
		return orderBooksVo;
	}

	@Override
	public OrderBooksVo viewRefund(String uid, Integer pageNumber, Integer pageSize) {
		List<OrderBookVo> orderBookVos = orderMapper.viewRefund(uid, pageNumber, pageSize);
		Integer total = orderMapper.viewRefundAll(uid);
		if (total == null) {
			total = 0;
		}
		OrderBooksVo orderBooksVo = new OrderBooksVo();
		orderBooksVo.setTotal(total);
		orderBooksVo.setOrderBooksVos(orderBookVos);
		return orderBooksVo;
	}

	@Override
	public List<OrderBookVo> getOrders() {
		return orderMapper.getOrders();
	}

	@Override
	public Order queryOrderNumber(String orderNumber) {
		return orderMapper.queryByOrderNumber(orderNumber);
	}

	public Order queryByOrderId(String orderId) {
		return null;
	}

	public boolean edit(String orderId, String transactionId) {
		return false;
	}
}
