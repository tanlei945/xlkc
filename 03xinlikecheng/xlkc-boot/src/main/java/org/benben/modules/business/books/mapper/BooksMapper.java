package org.benben.modules.business.books.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.books.entity.Books;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.books.vo.BookVo;

/**
 * @Description: 书籍表
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
public interface BooksMapper extends BaseMapper<Books> {

	@Select("select * from bb_books limit #{pageNumber},#{pageSize}")
	List<BookVo> queryBooks(@Param("pageNumber") Integer pageNumber,@Param("pageSize") Integer pageSize);

	@Select("select count(*) from bb_books")
	Integer queryAll();

	List<BookVo> queryByName(@Param("pageNumber") Integer pageNumber,@Param("pageSize") Integer pageSize, @Param("name") String name);


	Integer queryByNameAll(@Param("name") String name);
}
