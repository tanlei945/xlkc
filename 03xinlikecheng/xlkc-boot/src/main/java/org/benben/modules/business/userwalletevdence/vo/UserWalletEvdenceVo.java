package org.benben.modules.business.userwalletevdence.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class UserWalletEvdenceVo {
	@Excel(name = "真实姓名", width = 15)
	private java.lang.String realName;
	@Excel(name = "汇款凭证地址", width = 15)
	private java.lang.String picture;
}
