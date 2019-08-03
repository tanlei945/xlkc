package org.benben.modules.business.bookpreorder.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 书籍预购管理
 * @author： jeecg-boot
 * @date：   2019-07-12
 * @version： V1.0
 */
@Data
@TableName("bb_book_preorder")
public class BookPreorder implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户相关联id*/
	@Excel(name = "用户相关联id", width = 15)
	private java.lang.String uid;
	/**用户相关联id*/
	@Excel(name = "书籍相关联id", width = 15)
	private java.lang.String bookId;
	//书籍名称
	@TableField(exist = false)
	private java.lang.String bookName;
	//用户姓名
	@TableField(exist = false)
	private java.lang.String nickName;
	/**书籍预定姓名*/
	@Excel(name = "书籍预定姓名", width = 15)
	private java.lang.String name;
	/**预留手机号*/
	@Excel(name = "预留手机号", width = 15)
	private java.lang.String phone;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**createBy*/
	@Excel(name = "createBy", width = 15)
	private java.lang.String createBy;
	/**updateBy*/
	@Excel(name = "updateBy", width = 15)
	private java.lang.String updateBy;
	/**updateTime*/
	@Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
}
