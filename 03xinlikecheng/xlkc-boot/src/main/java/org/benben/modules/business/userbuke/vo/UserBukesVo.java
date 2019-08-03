package org.benben.modules.business.userbuke.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.benben.modules.business.userleave.vo.UserCourseStateVo;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
public class UserBukesVo {
	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "userBukeVos", width = 15)
	private List<UserBukeVo> userBukeVos;
}
