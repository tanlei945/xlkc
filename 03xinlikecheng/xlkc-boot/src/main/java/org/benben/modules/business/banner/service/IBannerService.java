package org.benben.modules.business.banner.service;

import org.benben.modules.business.banner.entity.Banner;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 轮播图管理
 * @author： jeecg-boot
 * @date：   2019-05-31
 * @version： V1.0
 */
public interface IBannerService extends IService<Banner> {

	Banner queryByType(Integer type);
}
