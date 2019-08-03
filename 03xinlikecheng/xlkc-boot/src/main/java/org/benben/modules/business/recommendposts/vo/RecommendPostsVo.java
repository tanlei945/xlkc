package org.benben.modules.business.recommendposts.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.benben.modules.business.recommendposts.entity.RecommendPosts;
import org.benben.modules.business.userbuke.vo.UserBukeVo;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 管理员推荐帖子
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
@Data
public class RecommendPostsVo implements Serializable {

	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "postsList", width = 15)
	private List<RecommendsVos> postsList;
}
