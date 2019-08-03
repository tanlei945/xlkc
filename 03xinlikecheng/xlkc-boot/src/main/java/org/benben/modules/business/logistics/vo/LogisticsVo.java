package org.benben.modules.business.logistics.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class LogisticsVo {
	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**书籍名字*/
	@Excel(name = "书籍名字", width = 15)
	private java.lang.String name;
	/**书籍图片地址*/
	@Excel(name = "书籍图片地址", width = 15)
	private java.lang.String bookUrl;
	/**运单号*/
	@Excel(name = "运单号", width = 15)
	private java.lang.String waybill;
	/**承运快递*/
	@Excel(name = "承运快递", width = 15)
	private java.lang.String express;
	/**订单相关id*/
	@Excel(name = "订单相关id", width = 15)
	private java.lang.String orderId;

	private java.lang.String json;
}
