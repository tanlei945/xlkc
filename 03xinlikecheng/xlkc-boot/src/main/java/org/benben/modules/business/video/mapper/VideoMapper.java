package org.benben.modules.business.video.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.benben.modules.business.video.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.benben.modules.business.video.vo.VideoVo;
import org.benben.modules.business.video.vo.VideosVo;

/**
 * @Description: 对视频的管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface VideoMapper extends BaseMapper<Video> {

	@Select("SELECT *  from bb_video  where video_class = '0' limit #{pageNumber},#{pageSize}")
	List<Video> queryByType(@Param("pageNumber") Integer pageNumber,@Param("pageSize") Integer pageSize);

	@Select("SELECT COUNT(*) as total from bb_video  where video_class = '0' ")
	Integer getTotal(@Param("pageNumber") Integer pageNumber,@Param("pageSize") Integer pageSizee);

//	@Select("SELECT id,video_type from bb_video where video_class = '1' and invitecode = #{invitecode} and state = 1 and video_type is not NULL")
//	@Select("SELECT v.id,v.video_type from bb_video v INNER JOIN bb_user_video b on v.id = b.vid  where b.uid = #{uid} and v.video_class = '1' and v.invitecode = #{invitecode} and b.state = 1 and v.video_type is not NULL")
	@Select("SELECT v.id,v.video_type from bb_video_type v INNER JOIN bb_user_video b on v.id = b.vid  where b.uid = #{uid} and v.video_class = '1' and v.invitecode = #{invitecode} and b.state = 1 ")
	List<Video> queryByInvitecode(@Param("invitecode") String invitecode, @Param("uid") String uid);

//	@Select("SELECT v.id,v.video_type from bb_video v INNER JOIN bb_user_video b on v.id=b.vid  where b.uid = #{uid} and b.state = 0 and v.video_class = 1 and v.video_type is not NULL")
	@Select("SELECT v.id,v.video_type from bb_video_type v INNER JOIN bb_user_video b on v.id=b.vid where b.uid = #{uid} and b.state = 0 and v.video_class = 1 ")
	List<VideoVo> queryVideo(String uid);

	@Update("update bb_user_video set state = 0 where uid = #{uid} and vid = #{parentId}")
	int updateByState(String uid,String parentId);

//	@Select( "SELECT v.* from bb_video v INNER JOIN bb_video b on v.parentid = b.id INNER JOIN bb_user_video bv on bv.vid = b.id  where bv.uid = #{uid} and v.parentid = #{parentId} and bv.state = '0' and v.invitecode = '' or v.invitecode is null" )
	@Select( "SELECT v.* from bb_video v INNER JOIN bb_video_type b on v.parentid = b.id INNER JOIN bb_user_video bv on bv.vid = b.id where bv.uid = #{uid} and v.parentid = #{parentId} and bv.state = '0'" )
	List<Video> queryByVideotype(@Param("parentId") String parentId,@Param("uid") String uid);

	@Select("SELECT * from bb_video where video_type = #{videoType} ")
	Video queryBytype(String videoType);

	@Select("SELECT id from bb_video_type where video_type !='' and video_type is not null")
	List<String> query();

	@Select("INSERT into bb_user_video (id,vid,uid,state) VALUES(#{id},#{uid},#{vid},1)")
	void addBBVideoUser(@Param("id") String id,@Param("uid") String uid, @Param("vid") String vid);
}
