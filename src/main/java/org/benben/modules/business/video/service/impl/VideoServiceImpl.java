package org.benben.modules.business.video.service.impl;

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
	public List<Video> queryByTypeAndInvitecode(String invitecode) {
		List<Video> videos = videoMapper.queryByInvitecode(invitecode);
		if(videos == null){
			return null;
		}
		for(int i = 0; i < videos.size(); i++) {
			videoMapper.updateByState(videos.get(i).getId());
		}
		return videos;
	}

	@Override
	public List<VideoVo> queryVideo() {
		List<VideoVo> videos = videoMapper.queryVideo();
		return videos;
	}

	@Override
	public List<Video> queryByVidoetype(String parentId) {
		return videoMapper.queryByVideotype(parentId);
	}
}
