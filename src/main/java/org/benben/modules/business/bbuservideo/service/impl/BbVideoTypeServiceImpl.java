package org.benben.modules.business.bbuservideo.service.impl;

import org.benben.modules.business.bbuservideo.entity.BbVideoType;
import org.benben.modules.business.bbuservideo.mapper.BbVideoTypeMapper;
import org.benben.modules.business.bbuservideo.service.IBbVideoTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 视频类型关联表
 * @author： jeecg-boot
 * @date：   2019-05-29
 * @version： V1.0
 */
@Service
public class BbVideoTypeServiceImpl extends ServiceImpl<BbVideoTypeMapper, BbVideoType> implements IBbVideoTypeService {

	@Autowired
	private BbVideoTypeMapper videoTypeMapper;

	@Override
	public BbVideoType queryByName(String videoType) {
		return videoTypeMapper.queryByName(videoType);
	}
}
