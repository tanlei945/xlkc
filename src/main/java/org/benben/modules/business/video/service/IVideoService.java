package org.benben.modules.business.video.service;

import org.benben.modules.business.video.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 对视频的管理
 * @author： jeecg-boot
 * @date：   2019-05-23
 * @version： V1.0
 */
public interface IVideoService extends IService<Video> {

	List<Video> queryByType();

	List<Video> queryByTypeAndInvitecode( String invitecode);

	List<Video> queryVideo();

}
