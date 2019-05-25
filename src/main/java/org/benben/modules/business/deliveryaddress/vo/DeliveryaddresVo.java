package org.benben.modules.business.deliveryaddress.vo;

import lombok.Data;
import org.benben.modules.business.books.vo.BookVo;
import org.benben.modules.business.deliveryaddress.entity.Deliveryaddress;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class DeliveryaddresVo {
	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "deliveryaddress", width = 15)
	private List<Deliveryaddress> deliveryaddress;
}
