package org.benben.modules.business.posts.vo;

import lombok.Data;
import org.benben.modules.business.posts.entity.Posts;
import org.benben.modules.business.recommendposts.entity.RecommendPosts;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class PostsVo {
	@Excel(name = "total", width = 15)
	private java.lang.Integer total;
	@Excel(name = "postsList", width = 15)
	private List<PostsVos> postsList;
}
