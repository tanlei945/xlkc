package org.benben.modules.business.usermessage.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class LikeVo {
	/**主键id*/
	@TableId(type = IdType.UUID)
	private java.lang.String id;

	private java.lang.String nickName;
	private String avatar;

	private java.lang.String postName;

	//0/是未读，1/是已读
	private java.lang.Integer type;
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;

}
