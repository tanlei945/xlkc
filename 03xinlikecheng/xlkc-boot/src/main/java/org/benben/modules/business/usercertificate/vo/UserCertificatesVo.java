package org.benben.modules.business.usercertificate.vo;

import lombok.Data;
import org.benben.modules.business.userleave.vo.UserCourseStateVo;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class UserCertificatesVo {
	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "userCertificateVos", width = 15)
	private List<UserCertificateVo> userCertificateVos;
}
