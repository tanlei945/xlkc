package org.benben.modules.business.video.service.impl;

import org.benben.modules.business.video.entity.Video;
import org.benben.modules.business.video.mapper.VideoMapper;
import org.benben.modules.business.video.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 学习园地视频管理
 * @author： jeecg-boot
 * @date：   2019-05-20
 * @version： V1.0
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {

	@Autowired
	private VideoMapper videoMapper;

	@Override
	public List<Video> queryByType() {
		List<Video> videos = videoMapper.queryByType();
		return videos;
	}

	@Override
	public List<Video> queryByTypeAndInvitecode(String invitecode) {
		List<Video> videos = videoMapper.queryByInvitecode(invitecode);
		for(int i = 0; i < videos.size(); i++) {
			videoMapper.updateByState(videos.get(i).getId());
		}
		return videos;
	}

	@Override
	public List<Video> queryVideo() {
		List<Video> videos = videoMapper.queryVideo();
		return videos;
	}
}
