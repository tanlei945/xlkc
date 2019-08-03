package org.benben.modules.business.aboutus.service.impl;

import org.benben.modules.business.aboutus.entity.AboutUs;
import org.benben.modules.business.aboutus.mapper.AboutUsMapper;
import org.benben.modules.business.aboutus.service.IAboutUsService;
import org.benben.modules.business.aboutus.vo.AboutUsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 关于我们和联系我们管理
 * @author： jeecg-boot
 * @date：   2019-07-18
 * @version： V1.0
 */
@Service
public class AboutUsServiceImpl extends ServiceImpl<AboutUsMapper, AboutUs> implements IAboutUsService {

	@Autowired
	private AboutUsMapper aboutUsMapper;

	@Override
	public AboutUsVo getAboutUs() {
		return aboutUsMapper.getAboutUs();
	}
}
