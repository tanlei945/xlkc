package org.benben.modules.business.books.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class Booksvos {
	private static final long serialVersionUID = 1L;

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
	/**书籍详情*/
	@Excel(name = "书籍详情", width = 15)
	private java.lang.String bookComment;
	/**书籍内容*/
	@Excel(name = "书籍内容", width = 15)
	private java.lang.String bookContent;
	/**书的库存量*/
	@Excel(name = "书的库存量", width = 15)
	private java.lang.Integer bookNum;
	/**书籍图片地址*/
	@Excel(name = "书籍图片地址", width = 15)
	private java.lang.String bookUrl;
	/**书籍图片地址*/
	@Excel(name = "书籍轮播图地址", width = 15)
	private java.util.List<String> bookImg;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**创建人id*/
	@Excel(name = "创建人id", width = 15)
	private java.lang.String createBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**修改人id*/
	@Excel(name = "修改人id", width = 15)
	private java.lang.String updateBy;
}


