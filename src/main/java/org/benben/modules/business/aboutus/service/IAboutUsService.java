package org.benben.modules.business.aboutus.service;

import org.benben.modules.business.aboutus.entity.AboutUs;
import com.baomidou.mybatisplus.extension.service.IService;
import org.benben.modules.business.aboutus.vo.AboutUsVo;

/**
 * @Description: 关于我们和联系我们管理
 * @author： jeecg-boot
 * @date：   2019-07-18
 * @version： V1.0
 */
public interface IAboutUsService extends IService<AboutUs> {

	AboutUsVo getAboutUs();

}
