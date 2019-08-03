package org.benben.modules.business.order.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.benben.modules.business.logistics.entity.Logistics;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 订单表管理
 * @author： jeecg-boot
 * @date：   2019-05-30
 * @version： V1.0
 */
@Data
@TableName("bb_order")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**地址相关联id*/
	@Excel(name = "用户相关联id", width = 15)
	private java.lang.String uid;
	@TableField(exist = false)
	private java.lang.String nickname;
	/**地址相关联id*/
	@Excel(name = "地址相关联id", width = 15)
	private java.lang.String addressId;
	@TableField(exist = false)
	private java.lang.String address;
	/**书籍相关联id*/
	@Excel(name = "书籍相关联id", width = 15)
	private java.lang.String bookId;
	@TableField(exist = false)
	private java.lang.String bookName;
	/**订单编号*/
	@Excel(name = "订单编号", width = 15)
	private java.lang.String ordernumber;
	/**书籍数量*/
	@Excel(name = "书籍数量", width = 15)
	private java.lang.Integer booknum;

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
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	private java.lang.String uodateBy;
	@TableField(exist = false)
	private Logistics logistics;

	public Object getOrderMoney() {
		return null;
	}

	public String getOrderId() {
		return null;
	}
}
