package org.benben.modules.business.usercertificate.entity;

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
 * @Description: 我的证书管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
@Data
@TableName("user_certificate")
public class UserCertificate implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;

	@Excel(name = "用户相关联id", width = 15)
	private java.lang.String uid;
	/**中文名或者英文名*/
	@Excel(name = "中文名或者英文名", width = 15)
	private java.lang.String name;
	/**coursename*/
	@Excel(name = "coursename", width = 15)
	private java.lang.String coursename;
	/**上传证书日期*/
	@Excel(name = "上传证书日期", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date date;
	/**imgUrl*/
	@Excel(name = "imgUrl", width = 15)
	private java.lang.String imgUrl;

	@Excel(name = "1/通过，0/未通过", width = 15)
	private java.lang.Integer state;
	/**createTime*/
	@Excel(name = "createTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	/**createBy*/
	@Excel(name = "createBy", width = 15)
	private java.lang.String createBy;
	/**updateTime*/
	@Excel(name = "updateTime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**updateBy*/
	@Excel(name = "updateBy", width = 15)
	private java.lang.String updateBy;
}
