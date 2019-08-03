package org.benben.modules.business.order.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.order.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.order.vo.OrderBookVo;

/**
 * @Description: 订单表管理
 * @author： jeecg-boot
 * @date：   2019-05-30
 * @version： V1.0
 */
public interface OrderMapper extends BaseMapper<Order> {

	@Select("select o.*,b.name,b.book_url,b.price from bb_order o inner join bb_books b on b.id = o.book_id where o.state = 1 and o.uid = #{uid} LIMIT #{pageNumber}, #{pageSize}")
	List<OrderBookVo> viewNonPayment(String uid, Integer pageNumber, Integer pageSize);

	@Select("select count(1) from bb_order o inner join bb_books b on b.id = o.book_id where o.state = 1 and o.uid = #{uid}")
	Integer viewNonPaymentAll(String uid);

	@Select("select o.*,b.name,b.book_url,b.price from bb_order o inner join bb_books b on b.id = o.book_id where o.state = 2 and o.uid =#{uid} LIMIT #{pageNumber}, #{pageSize}")
	List<OrderBookVo> viewPayment(String uid, Integer pageNumber, Integer pageSize);

	@Select("select count(1) from bb_order o inner join bb_books b on b.id = o.book_id where o.state = 2 and o.uid =#{uid} ")
	Integer viewPaymentAll(String uid);

	@Select("select o.*,b.name,b.book_url,b.price from bb_order o INNER JOIN bb_book_refund bf on o.id = bf.order_id inner join bb_books b on b.id = o.book_id where o.uid =#{uid}  and bf.status = 2 or bf.status = 3 LIMIT #{pageNumber}, #{pageSize}")
	List<OrderBookVo> viewRefund(String uid, Integer pageNumber, Integer pageSize);

	@Select("select count(1) from bb_order o INNER JOIN bb_book_refund bf on o.id = bf.order_id inner join bb_books b on b.id = o.book_id where o.state = 2 and o.uid =#{uid} and bf.status = 2 or bf.status = 3")
	Integer viewRefundAll(String uid);

	@Select("select o.id,b.`name` from bb_order o INNER JOIN bb_books b on o.book_id = b.id")
	List<OrderBookVo> getOrders();

	@Select("select * from bb_order where ordernumber = #{orderNumber}")
	Order queryByOrderNumber(String orderNumber);
}
