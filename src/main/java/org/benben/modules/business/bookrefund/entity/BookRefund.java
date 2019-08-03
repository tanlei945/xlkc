package org.benben.modules.business.bookrefund.entity;

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
 * @Description: 书籍退款表
 * @author： jeecg-boot
 * @date：   2019-06-05
 * @version： V1.0
 */
@Data
@TableName("bb_book_refund")
public class BookRefund implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户相关联id*/
	@Excel(name = "用户相关联id", width = 15)
	private java.lang.String uid;
	/**书籍相关id*/
	@Excel(name = "书籍相关id", width = 15)
	private java.lang.String bookId;
	/**订单相关id*/
	@Excel(name = "订单相关id", width = 15)
	private java.lang.String orderId;
	/**退款原因*/
	@Excel(name = "退款原因", width = 15)
	private java.lang.String reason;
	/**退款金额*/
	@Excel(name = "退款金额", width = 15)
	private java.math.BigDecimal money;
	/**上传图片地址*/
	@Excel(name = "上传图片地址", width = 15)
	private java.lang.String imgUrl;

	@Excel(name = "1/可以申请退款  2/申请中 3/退款完成", width = 15)
	private java.lang.Integer status;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.String createBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	private java.lang.String updateBy;

	@TableField(exist = false)
	private String bookName;

	@TableField(exist = false)
	private String nickName;

}
