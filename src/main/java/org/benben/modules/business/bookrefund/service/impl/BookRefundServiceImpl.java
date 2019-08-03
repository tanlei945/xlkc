package org.benben.modules.business.bookrefund.service.impl;

import org.benben.modules.business.bookrefund.entity.BookRefund;
import org.benben.modules.business.bookrefund.mapper.BookRefundMapper;
import org.benben.modules.business.bookrefund.service.IBookRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 书籍退款表
 * @author： jeecg-boot
 * @date：   2019-06-05
 * @version： V1.0
 */
@Service
public class BookRefundServiceImpl extends ServiceImpl<BookRefundMapper, BookRefund> implements IBookRefundService {

	@Autowired
	private BookRefundMapper bookRefundMapper;

	@Override
	public BookRefund queryByUidAndOrderId(String uid, String orderId) {
		return bookRefundMapper.queryByUidAndOrderId(uid,orderId);
	}
}
