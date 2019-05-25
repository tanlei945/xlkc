package org.benben.modules.business.video.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;
import org.benben.modules.business.video.entity.Video;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

@Data
public class VideosVo {
	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "videoList", width = 15)
	private List<Video> videoList;

}
