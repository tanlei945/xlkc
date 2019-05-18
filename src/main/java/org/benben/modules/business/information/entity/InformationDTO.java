package org.benben.modules.business.information.entity;

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
 * @Description: 课程预约的个人信息
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@Data
@TableName("bb_course_per_information")
public class InformationDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**课程id*/
	@Excel(name = "课程id", width = 15)
	private java.lang.String courseId;
	/**报名人名称*/
	@Excel(name = "报名人名称", width = 15)
	private java.lang.String name;
	/**联系电话*/
	@Excel(name = "联系电话", width = 15)
	private java.lang.String phone;
	/**联系邮箱*/
	@Excel(name = "联系邮箱", width = 15)
	private java.lang.String email;
	/**居住地*/
	@Excel(name = "居住地", width = 15)
	private java.lang.String address;
	/**所在公司*/
	@Excel(name = "所在公司", width = 15)
	private java.lang.String company;
	/**推荐人*/
	@Excel(name = "推荐人", width = 15)
	private java.lang.String referrer;
	/**凭证确认 1/ 是  0/否*/
	@Excel(name = "凭证确认 1/ 是  0/否", width = 15)
	private java.lang.Integer voucher;
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
