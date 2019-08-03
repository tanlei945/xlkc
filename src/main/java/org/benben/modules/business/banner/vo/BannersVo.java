package org.benben.modules.business.banner.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class BannersVo {
/*	private static final long serialVersionUID = 1L;

	*//**主键id*//*
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	*//**1/首页轮播图  2/论坛轮播图*//*
	@Excel(name = "1/首页轮播图  2/论坛轮播图", width = 15)
	private java.lang.Integer type;*/
	/**图片地址*/
	@Excel(name = "图片地址", width = 15)
	private List<ImgUrl> imgUrls;
/*	*//**上传时间*//*
	@Excel(name = "上传时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;
	*//**上传者*//*
	@Excel(name = "上传者", width = 15)
	private java.lang.String createBy;
	*//**更新时间*//*
	@Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;
	*//**更新者*//*
	@Excel(name = "更新者", width = 15)
	private java.lang.String updateBy;*/
}
