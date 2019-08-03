package org.benben.modules.business.course.entity;

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
 * @Description: 关于课程的管理
 * @author： jeecg-boot
 * @date：   2019-05-31
 * @version： V1.0
 */
@Data
@TableName("bb_course")
public class Course implements Serializable {
	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
	private java.lang.String courseName;
	/**课程描述*/
	@Excel(name = "课程描述", width = 15)
	private java.lang.String comment;
	@Excel(name = "课程描述", width = 15)
	private java.lang.String videoName;
	/**课程价格*/
	@Excel(name = "课程人民币价格", width = 15)
	private java.math.BigDecimal chinaPrice;
	/**课程价格*/
	@Excel(name = "课程港币价格", width = 15)
	private java.math.BigDecimal hkPrice;
	/**课程人数*/
	@Excel(name = "课程人数", width = 15)
	private java.lang.Integer num;
	/**课程人数*/
	@Excel(name = "申请人数", width = 15)
	private java.lang.Integer applyNum;
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
	@Excel(name = "课程轮播图地址", width = 15)
	private java.lang.String pictureUrl;
	/**courseImg*/
	@Excel(name = "课程封面地址", width = 15)
	private java.lang.String courseImg;
	/**课程视频地址*/
	@Excel(name = "课程视频地址", width = 15)
	private java.lang.String videoUrl;
/*	*//**课程预约确认 1/确认 0/待定*//*
	@Excel(name = "课程预约确认 1/确认 0/待定", width = 15)
	private java.lang.Integer courseVerify;
	*//**课程退款 0/可以申请退款 1/退款 2/退款完成*//*
	@Excel(name = "课程退款 0/可以申请退款 1/退款 2/退款完成", width = 15)
	private java.lang.Integer courseRefund;*/
	/**课程完成 1/已完成 0/未完成*//*
	@Excel(name = "课程完成 1/已完成 0/未完成", width = 15)
	private java.lang.Integer achieve;*/
	/**级别*/
	@Excel(name = "级别", width = 15)
	private java.lang.Integer level;
	/**几节课*/
	@Excel(name = "几节课", width = 15)
	private java.lang.Integer numCourse;
	/**一节课几天*/
	@Excel(name = "一节课几天", width = 15)
	private java.lang.Integer dayCourse;
	/**补课天数*/
	@Excel(name = "补课的天数", width = 15)
	private java.lang.Integer bukeNum;
	/**创建人时间*/
	@Excel(name = "创建人时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
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
	/**课程类型*/
/*	@Excel(name = "课程类型", width = 15)
	private java.lang.String courseType;*/
	/**可以申请助教人数*/
	@Excel(name = "可以申请助教人数", width = 15)
	private java.lang.Integer assistantNumber;
//	/**课程人数*/
//	@Excel(name = "课程人数", width = 15)
//	private java.lang.Integer enterNum;
	/**助教开通时间*/
	@Excel(name = "助教开通时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date openingTime;
	/**助教截止日期*/
	@Excel(name = "助教截止日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date expirationTime;
}
