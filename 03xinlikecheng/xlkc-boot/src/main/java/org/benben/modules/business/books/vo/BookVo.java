package org.benben.modules.business.books.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class BookVo {
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**书籍名字*/
	@Excel(name = "书籍名字", width = 15)
	private java.lang.String name;
	/**书籍简介*/
	@Excel(name = "书籍简介", width = 15)
	private java.lang.String bookintro;
	/**书籍价格*/
	@Excel(name = "书籍价格", width = 15)
	private java.math.BigDecimal price;
	/**购买人数*/
	@Excel(name = "购买人数", width = 15)
	private java.lang.Integer num;
}
