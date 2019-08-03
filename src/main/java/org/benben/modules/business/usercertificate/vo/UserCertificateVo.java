package org.benben.modules.business.usercertificate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class UserCertificateVo {
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
}
