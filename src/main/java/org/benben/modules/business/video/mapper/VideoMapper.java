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

	@Select("SELECT id,video_type from bb_video where video_class = '1' and invitecode = #{invitecode} and state = 1 and video_type is not NULL")
	List<Video> queryByInvitecode(String invitecode);

	@Select("SELECT id,video_type from bb_video where state = 0 and video_class = 1 and video_type is not NULL")
	List<VideoVo> queryVideo();

	@Update("update bb_video set state = 0 where id = #{id}")
	int updateByState(String id);

	@Select( "SELECT * from bb_video where parentid = #{parentId}" )
	List<Video> queryByVideotype(String parentId);

}
