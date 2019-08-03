package org.benben.modules.business.banner.service.impl;

import net.sf.json.JSONObject;
import org.benben.modules.business.banner.entity.Banner;
import org.benben.modules.business.banner.mapper.BannerMapper;
import org.benben.modules.business.banner.service.IBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 轮播图管理
 * @author： jeecg-boot
 * @date：   2019-05-31
 * @version： V1.0
 */
@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

	@Autowired
	private BannerMapper bannerMapper;

	@Override
	public Banner queryByType(Integer type) {


		return bannerMapper.queryByType(type);
	}

}
