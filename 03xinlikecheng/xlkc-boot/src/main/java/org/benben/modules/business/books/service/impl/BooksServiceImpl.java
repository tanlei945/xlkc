package org.benben.modules.business.books.service.impl;

import org.apache.commons.lang.StringUtils;
import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.mapper.BooksMapper;
import org.benben.modules.business.books.service.IBooksService;
import org.benben.modules.business.books.vo.BookVo;
import org.benben.modules.business.books.vo.BooksVo;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 书籍表
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
@Service
public class BooksServiceImpl extends ServiceImpl<BooksMapper, Books> implements IBooksService {

	@Autowired
	private BooksMapper booksMapper;
	@Autowired
	private OrderMapper orderMapper;

	@Override
	public BooksVo queryBooks(Integer pageNumber, Integer pageSize) {
		List<BookVo> books = booksMapper.queryBooks(pageNumber, pageSize);
		Integer total = booksMapper.queryAll();
		if (total == null){
			total = 0;
		}
		BooksVo booksVo = new BooksVo();
		booksVo.setVideoList(books);
		booksVo.setTotal(total);
		return booksVo;
	}

	@Override
	public BooksVo queryByName(Integer pageNumber, Integer pageSize, String name) {
		if (StringUtils.isBlank(name)){
			name = "";
		}
		List<BookVo> books = booksMapper.queryByName(pageNumber, pageSize,name);
		Integer total = booksMapper.queryByNameAll(name);
		if (total == null){
			total = 0;
		}
		BooksVo booksVo = new BooksVo();
		booksVo.setVideoList(books);
		booksVo.setTotal(total);
		return booksVo;
	}

	@Override
	public boolean updateNum(String id, Integer num) {
		return booksMapper.updateNum(id,num);
	}

	@Override
	public List<Books> getBooks() {
		return booksMapper.getBooks();
	}

	@Override
	public List<Books> recommendBooks() {
		List<Books> books = booksMapper.recommendBooks();
		List<Books> list1 = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			list1.add(books.get(i));
		}
		return list1;
	}

	@Override
	public Order getOrder(String ordernumber) {
		return booksMapper.getOrder(ordernumber);
	}
}
