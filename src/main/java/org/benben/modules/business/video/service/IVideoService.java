package org.benben.modules.business.video.service;

import org.benben.modules.business.video.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.video.vo.VideoVo;
import org.benben.modules.business.video.vo.VideosVo;

import java.util.List;

/**
 * @Description: 对视频的管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface IVideoService extends IService<Video> {

	VideosVo queryByType(Integer pageNumber, Integer pageSize);

	List<Video> queryByTypeAndInvitecode( String invitecode,String uid);

	List<VideoVo> queryVideo(String uid);

	List<Video> queryByVidoetype(String parentId,String uid);

	Video queryBytype(String videoType);

	List<String> query();

	void addBBVideoUser(String id, String uid, String s);
}
