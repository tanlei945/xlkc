package org.benben.modules.business.userresponse.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

@Data
public class UserResponseVo {
	private static final long serialVersionUID = 1L;

	/**主键ID*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;
	/**评论人id*/
	@Excel(name = "被回复人昵称", width = 15)
	private java.lang.String evaluateUserName;
	/**回复用户id*/
	@Excel(name = "回复用户昵称", width = 15)
	private java.lang.String userName;
	/**回复内容*/
	@Excel(name = "回复内容", width = 15)
	private java.lang.String comment;

	@Excel(name = "host", width = 15)
	private boolean host;

}
