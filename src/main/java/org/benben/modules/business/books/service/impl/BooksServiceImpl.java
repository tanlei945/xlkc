package org.benben.modules.business.books.service.impl;

import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.mapper.BooksMapper;
import org.benben.modules.business.books.service.IBooksService;
import org.benben.modules.business.books.vo.BookVo;
import org.benben.modules.business.books.vo.BooksVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.awt.print.Book;
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
}
