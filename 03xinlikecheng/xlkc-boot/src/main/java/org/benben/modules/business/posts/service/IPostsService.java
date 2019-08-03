package org.benben.modules.business.posts.service;

import org.benben.modules.business.posts.entity.Posts;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.posts.vo.PostsVo;

import java.util.List;

/**
 * @Description: 帖子的管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
public interface IPostsService extends IService<Posts> {

	PostsVo listNewPosts(Integer pageNumber, Integer pageSize);

	PostsVo listHottestPosts(Integer pageNumber, Integer pageSize);

	PostsVo queryNewPosts(String name, Integer pageNumber, Integer pageSize);

	PostsVo queryHottestPost(String name, Integer pageNumber, Integer pageSize);

	PostsVo queryCourseType(String courseTypeId, String name, Integer pageNumber, Integer pageSize);

	PostsVo getHottestPosts(String courseTypeId,String name, Integer pageNumber, Integer pageSize);

	List<String> hotSearch();

}
