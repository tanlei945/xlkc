package org.benben.modules.business.userwallet.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class UserWalletVo {
	/**id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
	private java.lang.String userId;

	/**钱包余额*/
	@Excel(name = "钱包余额", width = 15)
	private java.math.BigDecimal money;

	@Excel(name = "汇款凭证地址", width = 15)
	private java.lang.String picture;
}
