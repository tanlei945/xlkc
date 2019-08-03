package org.benben.modules.business.course.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class Courses {
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
	/**课程价格*/
	@Excel(name = "课程人民币价格", width = 15)
	private java.math.BigDecimal chinaPrice;
	/**课程价格*/
	@Excel(name = "课程港币价格", width = 15)
	private java.math.BigDecimal hkPrice;
	/**课程人数*/
	@Excel(name = "课程人数", width = 15)
	private java.lang.Integer num;
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
	private List<String> pictureUrl;
	/**courseImg*/
	@Excel(name = "课程封面地址", width = 15)
	private java.lang.String courseImg;
	/**课程视频地址*/
	@Excel(name = "课程视频地址", width = 15)
	private java.lang.String videoUrl;
	/**级别*/
	@Excel(name = "级别", width = 15)
	private java.lang.Integer level;
	/**几节课*/
	@Excel(name = "几节课", width = 15)
	private java.lang.Integer numCourse;
	/**一节课几天*/
	@Excel(name = "一节课几天", width = 15)
	private java.lang.Integer dayCourse;
	/**创建人时间*/
	@Excel(name = "创建人时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
}
