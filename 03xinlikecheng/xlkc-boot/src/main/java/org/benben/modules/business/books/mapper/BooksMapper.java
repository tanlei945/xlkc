package org.benben.modules.business.books.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.benben.modules.business.books.entity.Books;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.books.vo.BookVo;
import org.benben.modules.business.books.vo.BooksVo;
import org.benben.modules.business.order.entity.Order;

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

	@Select("select count(1) from bb_books where name like '%${name}%'")
	Integer queryByNameAll(@Param("name") String name);

	@Update("update bb_books set num = num - #{num} where id = #{id}")
	boolean updateNum(@Param("id") String id, @Param("num") Integer num);

	@Select("select * from bb_books")
	List<Books> getBooks();

	@Select("select * from bb_books ORDER BY num DESC")
	List<Books> recommendBooks();

	@Select("select * from bb_order where ordernumber = #{orderNumber}")
	Order getOrder(String orderNumber);

}
