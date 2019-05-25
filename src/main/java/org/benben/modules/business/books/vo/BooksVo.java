package org.benben.modules.business.books.vo;

import lombok.Data;
import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.video.entity.Video;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class BooksVo {
	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "videoList", width = 15)
	private List<BookVo> videoList;
}
