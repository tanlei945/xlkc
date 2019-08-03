package org.benben.modules.business.courserefund.entity;

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
 * @Description: 课程退款
 * @author： jeecg-boot
 * @date：   2019-06-03
 * @version： V1.0
 */
@Data
@TableName("bb_course_refund")
public class CourseRefund implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户相关联id*/
	@Excel(name = "用户相关联id", width = 15)
	private java.lang.String uid;
	/**课程相关id*/
	@Excel(name = "课程相关id", width = 15)
	private java.lang.String courseId;
	/**退款原因*/
	@Excel(name = "退款原因", width = 15)
	private java.lang.String reason;
	/**课程价格*/
	@Excel(name = "课程价格", width = 15)
	private java.math.BigDecimal money;
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

	//课程退款状态
	@TableField(exist = false)
	private Integer status;
	@TableField(exist = false)
	private String nickName;
	@TableField(exist = false)
	private String courseName;

}
