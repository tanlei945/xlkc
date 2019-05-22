package org.benben.modules.business.video.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.benben.modules.business.video.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 学习园地视频管理
 * @author： jeecg-boot
 * @date：   2019-05-20
 * @version： V1.0
 */
public interface VideoMapper extends BaseMapper<Video> {

	@Select("SELECT * from bb_video where video_class = '0'")
	List<Video> queryByType();

	@Select("SELECT * from bb_video where video_class = '1' and invitecode = #{invitecode} and state = 1")
	List<Video> queryByInvitecode(String invitecode);

	@Select("SELECT * from bb_video where state = 0 and video_class = 1")
	List<Video> queryVideo();

	@Update("update bb_video set state = 0 where id = #{id}")
	int updateByState(String id);
}
