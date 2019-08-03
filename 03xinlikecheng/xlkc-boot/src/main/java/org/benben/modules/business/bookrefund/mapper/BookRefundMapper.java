package org.benben.modules.business.bookrefund.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.bookrefund.entity.BookRefund;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 书籍退款表
 * @author： jeecg-boot
 * @date：   2019-06-05
 * @version： V1.0
 */
public interface BookRefundMapper extends BaseMapper<BookRefund> {

	@Select("select * from bb_book_refund where uid = #{uid} and order_id = #{orderId}")
	BookRefund queryByUidAndOrderId(String uid, String orderId);
}
