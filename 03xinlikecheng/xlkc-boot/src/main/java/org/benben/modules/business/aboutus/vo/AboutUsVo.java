package org.benben.modules.business.aboutus.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class AboutUsVo {
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
	/**微信二维码*/
	@Excel(name = "微信二维码", width = 15)
	private java.lang.Object wechatQrcode;
}
