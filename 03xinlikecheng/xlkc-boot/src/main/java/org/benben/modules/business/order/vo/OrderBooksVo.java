package org.benben.modules.business.order.vo;

import lombok.Data;
import org.benben.modules.business.userbuke.vo.UserBukeVo;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class OrderBooksVo {
	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "orderBookVos", width = 15)
	private List<OrderBookVo> orderBooksVos;
}
