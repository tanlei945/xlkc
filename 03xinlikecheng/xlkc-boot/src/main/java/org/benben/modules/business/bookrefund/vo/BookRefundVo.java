package org.benben.modules.business.bookrefund.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class BookRefundVo {
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
	/**退款金额*/
	@Excel(name = "退款金额", width = 15)
	private java.math.BigDecimal money;
}
