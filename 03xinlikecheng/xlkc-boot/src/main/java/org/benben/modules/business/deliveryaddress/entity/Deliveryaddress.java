package org.benben.modules.business.deliveryaddress.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * @Description: 收获地址管理
 * @author： jeecg-boot
 * @date：   2019-05-24
 * @version： V1.0
 */
@Data
@TableName("bb_deliveryaddress")
public class Deliveryaddress implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**收货人姓名*/
	@Excel(name = "收货人姓名", width = 15)
	private java.lang.String name;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
	private java.lang.String phone;
	/**收货地址*/
	@Excel(name = "收货地址", width = 15)
	private java.lang.String address;
	/**0/不是默认 1/默认地址*/
	@Excel(name = "0/不是默认 1/默认地址", width = 15)
	private java.lang.Integer state;
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
