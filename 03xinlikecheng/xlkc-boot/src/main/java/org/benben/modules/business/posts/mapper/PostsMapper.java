package org.benben.modules.business.posts.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.benben.modules.business.posts.entity.Posts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.usercollect.entity.UserCollect;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.security.PermitAll;

/**
 * @Description: 帖子的管理
 * @author： jeecg-boot
 * @date：   2019-06-10
 * @version： V1.0
 */
public interface PostsMapper extends BaseMapper<Posts> {

	@Select("SELECT * from bb_posts order by create_time desc LIMIT #{pageNumber}, #{pageSize}")
	List<Posts> queryNewPosts(@Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("SELECT count(1) from bb_posts order by create_time desc")
	Integer queryNewPostsAll();

	@Select("select count(1) from bb_like where post_id = #{id}")
	Integer getLikeNum(String id);

	@Select("select count(1) from user_evaluate where posts_id = #{id}")
	Integer getEvaluateNum(String id);

	@Select("SELECT * from bb_posts order by liknum desc LIMIT #{pageNumber}, #{pageSize}")
	List<Posts> queryHottestPosts(@Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("SELECT count(1) from bb_posts order by liknum desc")
	Integer queryHotestPostsAll();

	@Select("SELECT * from bb_posts  where posts_name like '%${name}%' order by create_time desc LIMIT #{pageNumber}, #{pageSize}")
	List<Posts> queryByName(@Param("name") String name, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("SELECT count(1) from bb_posts where posts_name like '%${name}%' order by create_time desc ")
	Integer queryByNameAll(@Param("name") String name);

	@Select("SELECT * from bb_posts where posts_name like '%${name}%' order by liknum desc LIMIT #{pageNumber}, #{pageSize}")
	List<Posts> queryByNameHottest(@Param("name") String name, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("SELECT count(1) from bb_posts where posts_name like  '%${name}%' order by liknum desc")
	Integer queryByNameHotestAll(@Param("name") String name);

	@Select("SELECT * from bb_posts  where course_type_id = #{courseTypeId} and posts_name like '%${name}%' order by create_time desc   LIMIT #{pageNumber}, #{pageSize}")
	List<Posts> queryCourseType(@Param("courseTypeId") String courseTypeId,@Param("name") String name, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);
	@Select("SELECT * from bb_posts  where course_type_id = 'e68c96e3f066ac2ac965b9f5818c21ff' and posts_name like '%次%' order by create_time desc  LIMIT 0,5")
	List<Posts> queryCourseType1();

	@Select("SELECT count(1) from bb_posts  where course_type_id = #{courseTypeId} and posts_name like '%${name}%' order by create_time desc")
	Integer queryCourseTypeAll(@Param("courseTypeId") String courseTypeId,@Param("name") String name);

	@Select("SELECT * from bb_posts where course_type_id = #{courseTypeId} and posts_name like '%${name}%' order by liknum desc  LIMIT #{pageNumber}, #{pageSize}")
	List<Posts> getHottestPosts(@Param("courseTypeId") String courseTypeId,@Param("name") String name, @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize);

	@Select("SELECT count(1) from bb_posts where course_type_id = #{courseTypeId}  and  posts_name like '%${name}%'  order by liknum desc")
	Integer getHottestPostsAll(@Param("courseTypeId") String courseTypeId,@Param("name") String name);

	@Select("select posts_name from bb_posts  ORDER BY search_num DESC")
	List<String> hotSearch();

	@Select("select type from bb_like where uid = #{uid} and post_id = #{postId}")
	Integer getLikeType(String uid, String postId);

	@Select("select * from user_collect where user_id = #{uid} and posts_id = #{postId}")
	UserCollect getCollectType(@Param("uid") String uid,@Param("postId") String postId);
}
