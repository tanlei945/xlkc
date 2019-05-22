package org.benben.modules.business.books.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.books.entity.Books;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 书籍表
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
public interface BooksMapper extends BaseMapper<Books> {

	@Select("select * from bb_books")
	List<Books> queryBooks();
}
