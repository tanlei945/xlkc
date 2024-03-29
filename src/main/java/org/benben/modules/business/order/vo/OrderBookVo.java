package org.benben.modules.business.order.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class OrderBookVo {
	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
	private java.lang.String ordernumber;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**书籍名字*/
	@Excel(name = "书籍名字", width = 15)
	private java.lang.String name;
	/**书籍相关联id*/
	@Excel(name = "书籍相关联id", width = 15)
	private java.lang.String bookId;
	/**书籍图片地址*/
	@Excel(name = "书籍图片地址", width = 15)
	private java.lang.String bookUrl;
	/**书籍价格*/
	@Excel(name = "书籍价格", width = 15)
	private java.math.BigDecimal price;
	/**订单总价*/
	@Excel(name = "订单总价", width = 15)
	private java.math.BigDecimal totalprice;

	/**书籍数量*/
	@Excel(name = "书籍数量", width = 15)
	private java.lang.Integer booknum;

	@Excel(name = "1/未付款，2/已付款，3/已发货，4/已收货", width = 15)
	private java.lang.Integer state;

	@Excel(name = "课程退款0/可以退款 1/退款中 2/退款完成 3/不可退款", width = 15)
	private java.lang.Integer status;
}
