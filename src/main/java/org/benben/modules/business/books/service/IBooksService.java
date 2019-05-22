package org.benben.modules.business.books.service;

import org.benben.modules.business.books.entity.Books;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 书籍表
 * @author： jeecg-boot
 * @date：   2019-05-22
 * @version： V1.0
 */
public interface IBooksService extends IService<Books> {

	List<Books> queryBooks();

}
