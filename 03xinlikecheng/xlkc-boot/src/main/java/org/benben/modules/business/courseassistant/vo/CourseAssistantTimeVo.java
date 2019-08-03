package org.benben.modules.business.courseassistant.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.benben.modules.business.courseassistanttime.vo.TimeVo;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class CourseAssistantTimeVo {
	@Excel(name = "课程相关联id", width = 15)
	private java.lang.String courseId;
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

	private List<TimeVo> timeVoList;
}
