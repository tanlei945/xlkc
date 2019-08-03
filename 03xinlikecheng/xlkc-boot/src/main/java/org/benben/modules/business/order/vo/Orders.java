package org.benben.modules.business.order.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.benben.modules.business.deliveryaddress.entity.Deliveryaddress;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class Orders {
	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**地址相关联id*/
	@Excel(name = "用户相关联id", width = 15)
	private java.lang.String uid;
	/**地址相关联id*/
	@Excel(name = "地址相关联id", width = 15)
	private Deliveryaddress address;
	/**书籍相关联id*/
	@Excel(name = "书籍相关联id", width = 15)
	private java.lang.String bookId;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
	private java.lang.String ordernumber;
	/**书籍数量*/
	@Excel(name = "书籍数量", width = 15)
	private java.lang.Integer booknum;
	/**书籍名字*/
	@Excel(name = "书籍名字", width = 15)
	private java.lang.String name;
	/**书籍价格*/
	@Excel(name = "书籍价格", width = 15)
	private java.math.BigDecimal price;

	@Excel(name = "书籍图片地址", width = 15)
	private java.lang.String bookUrl;
	//1/微信支付，2线下支付
	@Excel(name = "支付方式", width = 15)
	private java.lang.Integer paymode;
	/**订单总价*/
	@Excel(name = "订单总价", width = 15)
	private java.math.BigDecimal totalprice;
	/**备注*/
	@Excel(name = "备注", width = 15)
	private java.lang.String remark;

	@Excel(name = "1/未付款  2/已付款  3/已发货  4/已收货", width = 15)
	private java.lang.Integer state;

	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;

}
