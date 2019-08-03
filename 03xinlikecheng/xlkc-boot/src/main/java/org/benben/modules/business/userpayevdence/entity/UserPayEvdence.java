package org.benben.modules.business.userpayevdence.entity;

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
 * @Description: 支付凭证管理
 * @author： jeecg-boot
 * @date：   2019-06-19
 * @version： V1.0
 */
@Data
@TableName("user_pay_evdence")
public class UserPayEvdence implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户相关联id*/
	@Excel(name = "用户相关联id", width = 15)
	private java.lang.String uid;
	@TableField(exist = false)
	private java.lang.String nickname;
	/**汇款凭证地址*/
	@Excel(name = "汇款凭证地址", width = 15)
	private java.lang.String picture;
	/**订单id */
	@Excel(name = "订单编号id ", width = 15)
	private java.lang.String orderId;
	/**我的课程id*/
	@Excel(name = "我的课程id", width = 15)
	private java.lang.String userCourseId;
	/**支付凭证的确认*/
	@Excel(name = "支付凭证的确认", width = 15)
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
}
