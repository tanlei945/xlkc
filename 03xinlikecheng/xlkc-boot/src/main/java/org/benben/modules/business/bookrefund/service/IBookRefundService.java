package org.benben.modules.business.bookrefund.service;

import org.benben.modules.business.bookrefund.entity.BookRefund;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 书籍退款表
 * @author： jeecg-boot
 * @date：   2019-06-05
 * @version： V1.0
 */
public interface IBookRefundService extends IService<BookRefund> {

	BookRefund queryByUidAndOrderId(String id, String orderId);
}
