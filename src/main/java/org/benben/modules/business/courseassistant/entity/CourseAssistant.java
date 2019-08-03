package org.benben.modules.business.courseassistant.entity;

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
 * @Description: 关于助教的申请
 * @author： jeecg-boot
 * @date：   2019-06-06
 * @version： V1.0
 */
@Data
@TableName("bb_course_assistant")
public class CourseAssistant implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**课程相关联id*/
	@Excel(name = "课程相关联id", width = 15)
	private java.lang.String courseId;
	/*课程名称*/
	@TableField(exist = false)
	private java.lang.String courseName;
	/**课程相关联id*/
	@Excel(name = "用户关联id", width = 15)
	private java.lang.String userId;
	/**0/普通话 1/粤语*/
	@Excel(name = "0/普通话 1/粤语", width = 15)
	private java.lang.Integer language;
	/**所属公司名称*/
	@Excel(name = "所属公司名称", width = 15)
	private java.lang.String company;
	/**个人长处*/
	@Excel(name = "个人长处", width = 15)
	private java.lang.String forte;
	/**对学习者的贡献*/
	@Excel(name = "对学习者的贡献", width = 15)
	private java.lang.String skill;
	/**照片存放地址*/
	@Excel(name = "照片存放地址", width = 15)
	private java.lang.String picture;
	/**0/申请失败 1/成功*/
	@Excel(name = "0/申请失败 1/成功", width = 15)
	private java.lang.String state;
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
