package org.benben.modules.business.order.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class OrderVo {
	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**地址相关联id*/
	@Excel(name = "地址相关联id", width = 15)
	private java.lang.String addressId;
	/**书籍相关联id*/
	@Excel(name = "书籍相关联id", width = 15)
	private java.lang.String bookId;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
	private java.lang.String ordernumber;
	/**书籍数量*/
	@Excel(name = "书籍数量", width = 15)
	private java.lang.Integer booknum;
	/**订单总价*/
	@Excel(name = "订单总价", width = 15)
	private java.math.BigDecimal totalprice;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private java.lang.String remark;
	/**收货人姓名*/
	@Excel(name = "收货人姓名", width = 15)
	private java.lang.String name;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
	private java.lang.String phone;
	/**收货地址*/
	@Excel(name = "收货地址", width = 15)
	private java.lang.String address;

}
