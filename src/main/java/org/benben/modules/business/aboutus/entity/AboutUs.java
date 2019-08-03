package org.benben.modules.business.aboutus.entity;

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
 * @Description: 关于我们和联系我们管理
 * @author： jeecg-boot
 * @date：   2019-07-18
 * @version： V1.0
 */
@Data
@TableName("bb_about_us")
public class AboutUs implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**联系我们的电话*/
	@Excel(name = "联系我们的电话", width = 15)
	private java.lang.String contactPhone;
	/**公司邮箱*/
	@Excel(name = "公司邮箱", width = 15)
	private java.lang.String companyMail;
	/**公司地址*/
	@Excel(name = "公司地址", width = 15)
	private java.lang.String companyAddress;
	/**公司版本号*/
	@Excel(name = "公司版本号", width = 15)
	private java.lang.String versionsNumber;
	/**公司logo图标*/
	@Excel(name = "公司logo图标", width = 15)
	private java.lang.String companyLogo;
	/**微信二维码*/
	@Excel(name = "微信二维码", width = 15)
	private java.lang.String wechatQrcode;
	//type 0/联络我们 /1/关于我们
	@Excel(name = "状态", width = 15)
	private java.lang.Integer type;
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
