package org.benben.modules.business.books.service.impl;

import org.benben.modules.business.books.entity.Books;
import org.benben.modules.business.books.mapper.BooksMapper;
import org.benben.modules.business.books.service.IBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
	public List<Books> queryBooks() {
		return  booksMapper.queryBooks();
	}
}
