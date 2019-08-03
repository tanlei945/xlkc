package org.benben.modules.business.books.service;

import org.benben.modules.business.books.entity.Books;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.books.vo.BooksVo;
import org.benben.modules.business.order.entity.Order;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Book;
import java.util.List;

/**
 * @Description: 书籍表
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
public interface IBooksService extends IService<Books> {

	BooksVo queryBooks( Integer pageNumber,  Integer pageSize);

	BooksVo queryByName(Integer pageNumber, Integer pageSize, String name);

	boolean updateNum(String id, Integer num);

	List<Books> getBooks();

	List<Books> recommendBooks();

	Order getOrder(String ordernumber);
}
