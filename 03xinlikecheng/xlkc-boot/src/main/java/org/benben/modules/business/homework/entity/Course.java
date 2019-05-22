package org.benben.modules.business.homework.entity;

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
 * @Description: 关于课程的表
 * @author： jeecg-boot
 * @date：   2019-05-18
 * @version： V1.0
 */
@Data
@TableName("bb_course")
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    
	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.Integer id;
	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
	private java.lang.String courseName;
	/**课程描述*/
	@Excel(name = "课程描述", width = 15)
	private java.lang.String comment;
	/**课程价格*/
	@Excel(name = "课程价格", width = 15)
	private java.math.BigDecimal price;
	/**课程人数*/
	@Excel(name = "课程人数", width = 15)
	private java.lang.Integer num;
	/**上课时间*/
	@Excel(name = "上课时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date starttime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date endtime;
	/**上课地址*/
	@Excel(name = "上课地址", width = 15)
	private java.lang.String address;
	/**语种 0/普通话 1/粤语*/
	@Excel(name = "语种 0/普通话 1/粤语", width = 15)
	private java.lang.Integer language;
	/**上课简介*/
	@Excel(name = "上课简介", width = 15)
	private java.lang.String intro;
	/**课程部分内容*/
	@Excel(name = "课程部分内容", width = 15)
	private java.lang.String courseContent;
	/**课程图片地址*/
	@Excel(name = "课程图片地址", width = 15)
	private java.lang.String pictureUrl;
	/**课程视频地址*/
	@Excel(name = "课程视频地址", width = 15)
	private java.lang.String videoUrl;
	/**课程预约确认 1/确认 0/待定*/
	@Excel(name = "课程预约确认 1/确认 0/待定", width = 15)
	private java.lang.Integer courseVerify;
	/**课程退款 0/可以申请退款 1/退款 2/退款完成*/
	@Excel(name = "课程退款 0/可以申请退款 1/退款 2/退款完成", width = 15)
	private java.lang.Integer courseRefund;
	/**课程完成 1/已完成 0/未完成*/
	@Excel(name = "课程完成 1/已完成 0/未完成", width = 15)
	private java.lang.Integer achieve;
	/**创建人时间*/
	@Excel(name = "创建人时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTiem;
	/**创建人*/
	@Excel(name = "创建人", width = 15)
	private java.lang.Integer createBy;
	/**修改时间*/
	@Excel(name = "修改时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	/**修改人*/
	@Excel(name = "修改人", width = 15)
	private java.lang.Integer updateBy;
}
