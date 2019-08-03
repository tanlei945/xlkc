package org.benben.modules.business.video.service.impl;

import org.apache.commons.lang.StringUtils;
import org.benben.modules.business.video.entity.Video;
import org.benben.modules.business.video.mapper.VideoMapper;
import org.benben.modules.business.video.service.IVideoService;
import org.benben.modules.business.video.vo.VideoVo;
import org.benben.modules.business.video.vo.VideosVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 对视频的管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {

	@Autowired
	private VideoMapper videoMapper;

	@Override
	public VideosVo queryByType(Integer pageNumber, Integer pageSize) {
		List<Video> videos = videoMapper.queryByType(pageNumber,pageSize);
		Integer total = videoMapper.getTotal(pageNumber,pageSize);
		if (total == null) {
			total = 0;
		}
		VideosVo videosVo = new VideosVo();
		videosVo.setVideoList(videos);
		videosVo.setTotal(total);
		return videosVo;
	}

	@Override
	public List<Video> queryByTypeAndInvitecode(String invitecode,String uid) {
		List<Video> videos = videoMapper.queryByInvitecode(invitecode,uid);
		if(videos == null){
			return null;
		}
		for (Video video : videos) {
			if (StringUtils.isNotBlank(video.getId())){
				for(int i = 0; i < videos.size(); i++) {
					videoMapper.updateByState(uid,video.getId());
				}
			}

		}
		return videos;
	}

	@Override
	public List<VideoVo> queryVideo(String uid) {
		List<VideoVo> videos = videoMapper.queryVideo(uid);
		return videos;
	}

	@Override
	public List<Video> queryByVidoetype(String parentId,String uid) {
		return videoMapper.queryByVideotype(parentId,uid);
	}

	@Override
	public Video queryBytype(String videoType) {
		return videoMapper.queryBytype(videoType);
	}

	@Override
	public List<String> query() {
		return videoMapper.query();
	}

	@Override
	public void addBBVideoUser(String id,String uid, String s) {
		videoMapper.addBBVideoUser(id,uid,s);
	}
}
